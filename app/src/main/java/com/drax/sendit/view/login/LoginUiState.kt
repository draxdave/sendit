package com.drax.sendit.view.login

sealed class LoginUiState {
    object Neutral: LoginUiState()
    data class LoginFailed(val errorCode: Int, val message: String?): LoginUiState()
    object LoginSucceed: LoginUiState()

    object GoogleSignInClicked: LoginUiState()
    data class GoogleSignInFailed(val message: Int): LoginUiState()
}