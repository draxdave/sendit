package com.drax.sendit.view.qr

import androidx.compose.runtime.mutableStateOf
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.AppRetrofit
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.view.qr.components.QrLoadState
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

@HiltViewModel
class QrVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository
) : ResViewModel() {

    private val _uiState = MutableStateFlow<QrUiState>(QrUiState.Neutral)
    val uiState: StateFlow<QrUiState> = _uiState

    var qrState = mutableStateOf<QrLoadState>(QrLoadState.Loading)
        private set

    var qrPairState = mutableStateOf<QrPairState>(QrPairState.Neutral)
        private set

    private val _qrImageUrl = MutableStateFlow<String?>(null)
    val qrImageUrl: StateFlow<String?> = _qrImageUrl


    init {
        job {
            deviceRepository.getQrUrl().collect { imageUrl ->
                if (imageUrl == null) {
                    requestQrUrl()
                } else {
                    qrState.value = QrLoadState.Success(imageUrl)
                }
            }

        }
    }

    private suspend fun requestQrUrl() {
        when (val result = deviceRepository.getQRUrlFromServer()) {
            is Resource.ERROR -> Unit
            is Resource.SUCCESS -> when (val qrUrl = result.data.data?.qrUrl) {
                null -> Unit
                else -> {
                    val fullUrl = AppRetrofit.BaseUrl + qrUrl
                    deviceRepository.storeQRUrl(fullUrl)
                    withContext(Dispatchers.Main) {
                        qrState.value = QrLoadState.Success(fullUrl)
                    }
                }
            }
        }
    }

    fun sendPairRequest(requestCode: String) {
        _uiState.update { QrUiState.Loading }

        job {
            connectionRepository.sendPairRequest(PairRequest(requestCode)).collect { response ->

                _uiState.update { QrUiState.Neutral }
                val newState = when (response) {
                    is Resource.ERROR -> when (response.errorCode) {
                        PairResponse.ALREADY_ACTIVE -> QrPairState.ConnectionAlreadyActive
                        PairResponse.REJECTED -> QrPairState.RequestRejected
                        PairResponse.WAITING_FOR_PEER -> QrPairState.InvitationResponseWaiting
                        else -> QrPairState.PairFailed(response)
                    }

                    is Resource.SUCCESS -> QrPairState.PairDone(
                        response.data.data?.connection
                            ?: return@collect
                    )
                }
                withContext(Dispatchers.Main) {
                    qrPairState.value = newState
                }
            }
        }
    }
}