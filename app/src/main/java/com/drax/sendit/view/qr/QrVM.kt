package com.drax.sendit.view.qr

import com.drax.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.network.model.PairResponseRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

class QrVM(
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository
): ResViewModel() {

    private val _uiState = MutableStateFlow<QrUiState>(QrUiState.Neutral)
    val uiState: StateFlow<QrUiState> = _uiState

    private val _state = MutableSharedFlow<QrState>(replay = 0, extraBufferCapacity = 1, BufferOverflow.DROP_OLDEST)
    val state = _state.asSharedFlow()

    private val _qrImageUrl = MutableStateFlow<String?>(null)
    val qrImageUrl : StateFlow<String?> = _qrImageUrl


    init {
        job {
            deviceRepository.getQrUrl().collect { imageUrl ->
                if (imageUrl == null) {
                    requestQrUrl()
                } else {
                    _qrImageUrl.update { imageUrl }
                }
            }

        }
    }

    private suspend fun requestQrUrl(){
        _uiState.update { QrUiState.QrLoading}
        _state.tryEmit(
            when(val result = deviceRepository.getQRUrlFromServer()){
                is Resource.ERROR -> QrState.QrLoadFailedFromNet(result)
                is Resource.SUCCESS -> when(val qrUrl = result.data.data?.qrUrl){
                    null -> QrState.QrLoadFailed(R.string.unknown_error)
                    else -> {
                        val fullUrl = AppRetrofit.getBaseUrl() + qrUrl
                        deviceRepository.storeQRUrl(fullUrl)
                        QrState.QrLoaded(fullUrl)
                    }
                }
            }
        )
        _uiState.update { QrUiState.Neutral}
    }

    fun sendPairRequest(requestCode: String){
        _uiState.update { QrUiState.QrLoading }

        job {
            connectionRepository.sendInvitation(PairRequest(requestCode)).collect {response->

                _state.tryEmit(
                    when(response){
                        is Resource.ERROR -> when(response.errorCode) {
                            PairResponse.ALREADY_ACTIVE -> QrState.InvitationResponseAlreadyActive
                            PairResponse.REJECTED -> QrState.InvitationResponseRejected
                            PairResponse.WAITING_FOR_PEER -> QrState.InvitationResponseWaiting
                            else -> QrState.InvitationResponseFailed(response)
                        }
                        is Resource.SUCCESS -> QrState.InvitationSent
                    }
                )
            }
            _uiState.update { QrUiState.Neutral}
        }
    }

    fun sendInvitationResponse(connectionId: Long, response: Int){
        _uiState.update { QrUiState.QrLoading }
        job {
            connectionRepository.invitationResponse(PairResponseRequest(connectionId, response)).collect {invResponse->
                _state.tryEmit(
                    when(invResponse){
                        is Resource.ERROR -> QrState.InvitationResponseFailed(invResponse)
                        is Resource.SUCCESS ->QrState.InvitationResponseSent
                    }
                )
            }
            _uiState.update { QrUiState.Neutral}
        }
    }
}