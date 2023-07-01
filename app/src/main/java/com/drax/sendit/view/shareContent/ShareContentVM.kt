package com.drax.sendit.view.shareContent

import android.util.Log
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.view.connections.DeviceWrapper
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ShareContentVM @Inject constructor(
    private val pushRepository: PushRepository,
    private val transactionRepository: TransactionRepository,
    private val connectionRepository: ConnectionRepository
) : ResViewModel() {

    private val _uiState = MutableStateFlow<ShareContentUiState>(ShareContentUiState.Loading)
    val uiState: StateFlow<ShareContentUiState> = _uiState

    init {
        job {
            connectionRepository.getConnections(onlyActive = true).collect { connections ->
                Log.e("ShareContentScreen", "viewmodel uiState: ${this@ShareContentVM}")
                _uiState.update {
                    if (connections.isEmpty())
                        ShareContentUiState.NoConnectionsAvailable
                    else
                        ShareContentUiState.ConnectionsLoaded(connections.map {
                            DeviceWrapper(it)
                        })
                }
            }
        }
    }


    fun share(content: String, connectionId: Long) {
        _uiState.update { ShareContentUiState.Loading }

        job {
            pushRepository.shareContent(
                ShareRequest(
                    connectionId = connectionId,
                    content = content,
                    type = TransactionContentType.TransactionType_CONTENT_TEXT
                )
            ).collect { shareResponse ->
                _uiState.update {
                    when (shareResponse) {
                        is Resource.ERROR -> ShareContentUiState.SharingFailed(shareResponse)
                        is Resource.SUCCESS -> {
                            shareResponse.data.data?.transaction?.let {
                                transactionRepository.insertNewTransaction(
                                    it
                                )
                            }
                            ShareContentUiState.SharingDone
                        }
                    }
                }

            }
        }
    }

}
