package com.drax.sendit.view.qr

import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
                        val fullUrl = AppRetrofit.BaseUrl + qrUrl
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
            connectionRepository.sendPairRequest(PairRequest(requestCode)).collect { response->

                _uiState.update { QrUiState.Neutral}
                _state.tryEmit(
                    when(response){
                        is Resource.ERROR -> when(response.errorCode) {
                            PairResponse.ALREADY_ACTIVE -> QrState.ConnectionAlreadyActive
                            PairResponse.REJECTED -> QrState.RequestRejected
                            PairResponse.WAITING_FOR_PEER -> QrState.InvitationResponseWaiting
                            else -> QrState.PairFailed(response)
                        }
                        is Resource.SUCCESS -> QrState.PairDone(
                            response.data.data?.connection
                                ?: return@collect
                        )
                    }
                )
            }
        }
    }
}