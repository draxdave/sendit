package com.drax.sendit.view.connections.unpair

import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.view.connections.ConnectionUiState
import com.drax.sendit.view.main.MainUiState
import com.drax.sendit.view.shareContent.ShareContentUiState
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

class UnpairVM(
    private val connectionRepository: ConnectionRepository
    ): ResViewModel() {

    private val _uiState = MutableStateFlow<UnpairUiState>(UnpairUiState.Neutral)
    val uiState: StateFlow<UnpairUiState> = _uiState


    fun unpairDevice(unpairRequest: UnpairRequest){
        _uiState.update { UnpairUiState.Loading }
        job {
            connectionRepository.unpair(unpairRequest).collect { unpair->
                _uiState.update {
                    when(unpair){
                        is Resource.ERROR -> UnpairUiState.Failed(unpair)
                        is Resource.SUCCESS -> {
                            UnpairUiState.Done
                        }
                    }
                }
            }
        }
    }
}