package com.drax.sendit.view.qr

import com.drax.sendit.data.model.Resource

sealed class QrUiState {
    object Neutral: QrUiState()
    object QrLoading: QrUiState()
    data class QrLoaded(val qrUrl: String): QrUiState()
    data class QrLoadFailed(val reason: Int): QrUiState()
    data class QrLoadFailedFromNet(val reason: Resource.ERROR): QrUiState()

    object InvitationSending: QrUiState()
    data class InvitationFailed(val reason: Resource.ERROR): QrUiState()
    object InvitationSent: QrUiState()

    object InvitationResponseSending: QrUiState()
    object InvitationResponseSent: QrUiState()
    data class InvitationResponseFailed(val reason: Resource.ERROR): QrUiState()

    object InvitationResponseAlreadyActive: QrUiState()
    object InvitationResponseRejected: QrUiState()
    object InvitationResponseWaiting: QrUiState()
}