package com.drax.sendit.view.login

sealed class LoginUiState {
    object Neutral: LoginUiState()
    data class LoginFailed(val errorCode: Int): LoginUiState()
    object LoginSucceed: LoginUiState()
    object Loading: LoginUiState()

    data class GoogleSignInFailed(val message: Int): LoginUiState()
}