package com.drax.sendit.view.main

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource

sealed class MainUiState{
    object Neutral: MainUiState()
    object UserSignedIn: MainUiState()
    object UserSignedOut: MainUiState()

    data class ShareModalDisplayed(val shareText: String, val connections: List<Connection>): MainUiState()
    object NoConnectionModal: MainUiState()

    object Sharing: MainUiState()
    object SharingDone: MainUiState()
    data class SharingFailed(val reason: Resource.ERROR): MainUiState()

}
