package com.drax.sendit.view.main

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.*

class MainVM(
    userRepository: UserRepository,
    deviceRepository: DeviceRepository,
    private val connectionRepository: ConnectionRepository,
    private val pushRepository: PushRepository
) : ResViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Neutral)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        job {
            userRepository.getUser().collect{ user ->
                _uiState.update {
                    when {
                        user == null ||
                                deviceRepository.getSelfDevice() == null
                        -> MainUiState.UserSignedOut
                        else -> MainUiState.UserSignedIn
                    }
                }
            }
        }
    }

    fun displayShareModal(shareText: String){
        job {
            connectionRepository.getConnections().collect {connections->
                _uiState.update {
                    if (connections.isEmpty())
                        MainUiState.NoConnectionModal
                    else
                        MainUiState.ShareModalDisplayed(shareText,connections)
                }
            }
        }
    }

    fun share(content: String, connectionId: Long){
        _uiState.update { MainUiState.Sharing}

        job {
            pushRepository.shareContent(ShareRequest(
                connectionId = connectionId,
                content = content,
                type = TransactionContentType.TransactionType_CONTENT_TEXT
            )).collect {shareResponse->
                _uiState.update {
                    when(shareResponse){
                        is Resource.ERROR -> MainUiState.SharingFailed(shareResponse)
                        is Resource.SUCCESS -> MainUiState.SharingDone
                    }
                }

            }
        }
    }
}