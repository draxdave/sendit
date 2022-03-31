package com.drax.sendit.view.login

import androidx.lifecycle.viewModelScope
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.model.LoginUiState
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.UiState
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val authRepository: AuthRepository
): ResViewModel() {
    val versionText="Version A.${BuildConfig.VERSION_NAME}"
    private val _uiState = MutableStateFlow<LoginUiState>(UiState.Neutral)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(signInRequest: SignInRequest) = viewModelScope.launch(Dispatchers.IO) {

        authRepository.signInDevice(signInRequest).collect {result->
            setLoading(false)

            _uiState.update {
                when(result){
                    is Resource.ERROR -> LoginUiState.LoginFailed(result.errorCode, result.message)
                    is Resource.SUCCESS -> LoginUiState.LoginSucceed
                }
            }
        }
    }

    fun googleSignInClicked() {
        setLoading(true)
        _uiState.update { LoginUiState.GoogleSignInClicked }
    }
    fun googleSignInFailed(message: Int){
        _uiState.update { LoginUiState.GoogleSignInFailed(message) }
        setLoading(false)
    }
}