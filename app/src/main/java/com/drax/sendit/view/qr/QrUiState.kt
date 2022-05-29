package com.drax.sendit.view.qr

import com.drax.sendit.data.model.Resource

sealed class QrUiState {
    object Neutral: QrUiState()
    object QrLoading: QrUiState()
}

sealed class QrState {
    data class QrLoaded(val qrUrl: String): QrState()
    data class QrLoadFailed(val reason: Int): QrState()
    data class QrLoadFailedFromNet(val reason: Resource.ERROR): QrState()

    data class InvitationFailed(val reason: Resource.ERROR): QrState()
    object InvitationSent: QrState()

    object InvitationResponseSent: QrState()
    data class InvitationResponseFailed(val reason: Resource.ERROR): QrState()

    object InvitationResponseAlreadyActive: QrState()
    object InvitationResponseRejected: QrState()
    object InvitationResponseWaiting: QrState()
}