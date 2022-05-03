package com.drax.sendit.view.main

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource

sealed class MainUiState{
    object Neutral: MainUiState()
    object UserSignedIn: MainUiState()
    object UserSignedOut: MainUiState()
}
