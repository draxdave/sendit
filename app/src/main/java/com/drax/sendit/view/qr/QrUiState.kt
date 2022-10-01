package com.drax.sendit.view.qr

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ConnectionModel

sealed class QrUiState {
    object Neutral: QrUiState()
    object QrLoading: QrUiState()
}

sealed class QrState {
    data class QrLoaded(val qrUrl: String): QrState()
    data class QrLoadFailed(val reason: Int): QrState()
    data class QrLoadFailedFromNet(val reason: Resource.ERROR): QrState()

    data class PairDone(val newConnection: ConnectionModel): QrState()
    data class PairFailed(val reason: Resource.ERROR): QrState()

    object ConnectionAlreadyActive: QrState()
    object RequestRejected: QrState()
    object InvitationResponseWaiting: QrState()
}