package com.drax.sendit.view.login

sealed class LoginUiState {
    object Neutral: LoginUiState()
    data class LoginFailed(val errorCode: Int = LOGIN_GENERAL_ERROR): LoginUiState()
    object LoginSucceed: LoginUiState()
    object Loading: LoginUiState()
    object ForgetPasswordDone: LoginUiState()
    object SignupDone: LoginUiState()

    data class GoogleSignInFailed(val message: Int): LoginUiState()

    companion object {
        const val LOGIN_GENERAL_ERROR = -1
    }
}

sealed class SigninFormState {
    object Signin: SigninFormState()
    object Signup: SigninFormState()
    object Forgot: SigninFormState()
    object FormHidden: SigninFormState()
}