package com.drax.sendit.view.login

import androidx.lifecycle.viewModelScope
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val connectionRepository: ConnectionRepository,
): ResViewModel() {
    val versionText="Version A.${BuildConfig.VERSION_NAME}"
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Neutral)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(signInRequest: SignInRequest) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {LoginUiState.Loading}

        authRepository.signInDevice(signInRequest).collect {result->

            _uiState.update {
                when(result){
                    is Resource.ERROR -> LoginUiState.LoginFailed(result.errorCode)
                    is Resource.SUCCESS -> {
                        result.data.data?.let {
                            storeToken(it.token)
                            storeDevices(it.device)
                            storeUser(it.user)
                        }
                        result.data.data?.connections?.let { storeConnections(*it.toTypedArray()) }
                        LoginUiState.LoginSucceed
                    }
                }
            }
        }
    }

    private fun storeToken(token: String){
        job {
            deviceRepository.storeToken(token)
        }
    }

    private fun storeUser(user: User){
        job {
            userRepository.addOrUpdateUser(user)
        }
    }

    private fun storeDevices(device: Device){
        job {
            deviceRepository.addOrUpdateDevice(device)
        }
    }

    private fun storeConnections(vararg connection: Connection){
        job {
            connectionRepository.addConnection(*connection)
        }
    }

    fun googleSignInFailed(message: Int){
        _uiState.update { LoginUiState.GoogleSignInFailed(message) }
    }
}