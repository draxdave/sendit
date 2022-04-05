package com.drax.sendit.view.qr

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.network.model.PairResponseRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

class QrVM(
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository
): ResViewModel() {

    private val _uiState = MutableStateFlow<QrUiState>(QrUiState.QrLoading)
    val uiState: StateFlow<QrUiState> = _uiState

    init {
        job {
            deviceRepository.getQrUrl().collect { qrUrl ->
                if (qrUrl == null) {
                    requestQrUrl()

                }else {
                    _uiState.update { QrUiState.QrLoaded(qrUrl)}
                }
            }
        }
    }

    private suspend fun requestQrUrl(){
        _uiState.update { QrUiState.QrLoading}
        _uiState.update {
            when(val result = deviceRepository.getQRUrlFromServer()){
                is Resource.ERROR -> QrUiState.QrLoadFailedFromNet(result)
                is Resource.SUCCESS -> when(val qrUrl = result.data.data?.qrUrl){
                    null -> QrUiState.QrLoadFailed(R.string.unknown_error)
                    else -> {
                        val fullUrl = BuildConfig.BASE_URL + qrUrl
                        deviceRepository.storeQRUrl(fullUrl)
                        QrUiState.QrLoaded(fullUrl)
                    }
                }
            }
        }
    }

    fun sendPairRequest(requestCode: String){
        _uiState.update { QrUiState.InvitationSending }

        job {
            connectionRepository.sendInvitation(PairRequest(requestCode)).collect {response->
                _uiState.update {
                    when(response){
                        is Resource.ERROR -> when(response.errorCode) {
                            PairResponse.ALREADY_ACTIVE -> QrUiState.InvitationResponseSending
                            PairResponse.REJECTED -> QrUiState.InvitationResponseRejected
                            PairResponse.WAITING_FOR_PEER -> QrUiState.InvitationResponseWaiting
                            else -> QrUiState.InvitationResponseFailed(response)
                        }
                        is Resource.SUCCESS -> QrUiState.InvitationSent
                    }
                }
            }
        }
    }

    fun sendInvitationResponse(connectionId: Long, response: Int){
        _uiState.update { QrUiState.InvitationResponseSending }
        job {
            connectionRepository.invitationResponse(PairResponseRequest(connectionId, response)).collect {invResponse->
                _uiState.update {
                    when(invResponse){
                        is Resource.ERROR -> QrUiState.InvitationResponseFailed(invResponse)
                        is Resource.SUCCESS ->QrUiState.InvitationResponseSent
                    }
                }
            }
        }
    }
}