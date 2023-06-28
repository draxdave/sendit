package com.drax.sendit.view.qr

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ConnectionModel

sealed class QrUiState {
    object Neutral : QrUiState()
    object Loading : QrUiState()
}

sealed class QrPairState {
    object Neutral : QrPairState()
    data class PairDone(val newConnection: ConnectionModel) : QrPairState()
    data class PairFailed(val reason: Resource.ERROR) : QrPairState()

    object ConnectionAlreadyActive : QrPairState()
    object RequestRejected : QrPairState()
    object InvitationResponseWaiting : QrPairState()
}