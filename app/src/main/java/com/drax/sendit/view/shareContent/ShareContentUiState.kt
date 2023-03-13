package com.drax.sendit.view.shareContent

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource

sealed class ShareContentUiState {
    object Loading : ShareContentUiState()
    data class ConnectionsLoaded(val connections: List<Connection>) : ShareContentUiState()
    object NoConnectionsAvailable : ShareContentUiState()

    object SharingDone : ShareContentUiState()
    data class SharingFailed(val reason: Resource.ERROR) : ShareContentUiState()
}