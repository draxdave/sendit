package com.drax.sendit.view.connections.unpair

import com.drax.sendit.data.model.Resource

sealed class UnpairUiState {
    object Loading: UnpairUiState()
    object Neutral: UnpairUiState()
    data class Failed(val reason: Resource.ERROR): UnpairUiState()
    object Done: UnpairUiState()
}