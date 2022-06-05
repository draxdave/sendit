package com.drax.sendit.view.main

sealed class MainUiState{
    object Neutral: MainUiState()
    object UserSignedIn: MainUiState()
    object UserSignedOut: MainUiState()
}
