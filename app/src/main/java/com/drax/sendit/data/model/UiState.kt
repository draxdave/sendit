package com.drax.sendit.data.model

sealed class UiState{
    object Neutral: LoginUiState()
}

sealed class LoginUiState: UiState() {
    data class LoginFailed(val errorCode: Int, val message: String?): LoginUiState()
    object LoginSucceed: LoginUiState()

    object GoogleSignInClicked: LoginUiState()
    data class GoogleSignInFailed(val message: Int): LoginUiState()
}