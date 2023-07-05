package com.drax.sendit.view.connections

import com.drax.sendit.data.model.Resource

sealed class ConnectionUiState {
    object Neutral : ConnectionUiState()
    object NoConnection : ConnectionUiState()
    data class ConnectionsLoaded(val connectionList: List<DeviceWrapper>) : ConnectionUiState()
    object Refreshing : ConnectionUiState()
    data class RefreshConnectionListFailed(val error: Resource.ERROR) : ConnectionUiState()
}
