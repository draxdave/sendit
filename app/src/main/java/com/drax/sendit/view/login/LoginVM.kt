package com.drax.sendit.view.login

import app.siamak.sendit.BuildConfig
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginVM(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val connectionRepository: ConnectionRepository,
    private val analytics: Analytics,
): ResViewModel() {
    val versionText="Version A.${BuildConfig.VERSION_NAME}"
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Neutral)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(signInRequest: SignInRequest) = job {
        _uiState.update {LoginUiState.Loading}

        when(val result = authRepository.signInDevice(signInRequest)){
            is Resource.ERROR -> _uiState.update { LoginUiState.LoginFailed(result.errorCode)}
            is Resource.SUCCESS -> {
                _uiState.update {   LoginUiState.LoginSucceed}
                delay(4000)
                analytics.set(Event.SignIn.GoToHome)
                result.data.data?.let { storeData(it)}
            }
        }
    }
    private fun storeData(signInResponse: SignInResponse) {
        storeToken(signInResponse.token)
        storeDevices(signInResponse.device)
        storeUser(signInResponse.user)
        signInResponse.connections?.let { storeConnections(*it.toTypedArray()) }
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