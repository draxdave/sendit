package com.drax.sendit.view.login

import android.util.Log
import app.siamak.sendit.BuildConfig
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.auth.ForgotPasswordRequest
import com.drax.sendit.domain.network.model.auth.SignUpRequest
import com.drax.sendit.domain.network.model.auth.signin.SignInRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.domain.network.model.device.WhoisModel
import com.drax.sendit.domain.network.model.device.toWhoisModel
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job

@HiltViewModel
class LoginVM @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val analytics: Analytics,
) : ResViewModel() {
    val versionText = "Version A.${BuildConfig.VERSION_NAME}"
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Neutral)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _signinFormState = MutableStateFlow<SigninFormState>(SigninFormState.Signin)
    val signinFormState: StateFlow<SigninFormState> = _signinFormState


    fun updateSigninFormState(newState: SigninFormState) = _signinFormState.update { newState }

    fun authoriseWithSso(signInRequest: SignInSsoRequest) = job {
        _uiState.update { LoginUiState.Loading }

        when (val result = authRepository.signInSso(signInRequest)) {
            is Resource.ERROR -> _uiState.update { LoginUiState.LoginFailed(result.errorCode) }
            is Resource.SUCCESS -> {
                _uiState.update {
                    authorised(result.data.data?.token ?: return@update LoginUiState.LoginFailed())
                    LoginUiState.LoginSucceed
                }
            }
        }
    }

    private suspend fun authorised(token: String) {
        storeToken(token)

        val (user, device) = getWhois() ?: return
        analytics.set(Event.SignIn.GoToHome)
        storeLoginData(
            user = user,
            device = device
        )
    }

    private suspend fun getWhois(): WhoisModel? {
        return when (val result = deviceRepository.getWhois()) {
            is Resource.ERROR -> {
                _uiState.tryEmit(LoginUiState.LoginFailed(result.errorCode))
                null
            }
            is Resource.SUCCESS -> result.data.data?.toWhoisModel()
        }
    }

    private suspend fun storeLoginData(device: DeviceDomain, user: User) {
        storeDevices(device)
        storeUser(user)
    }

    private suspend fun storeToken(token: String) {
        deviceRepository.storeToken(token)
    }

    private suspend fun storeUser(user: User) {
        userRepository.addOrUpdateUser(user)
    }

    private suspend fun storeDevices(device: DeviceDomain) {
        deviceRepository.addOrUpdateDevice(device)
    }

    fun googleSignInFailed(message: Int) {
        _uiState.update { LoginUiState.GoogleSignInFailed(message) }
    }

    fun forgetPassword(email: String) {
        job {
            _uiState.update { LoginUiState.Loading }
            when (val result = authRepository.forgotPassword(ForgotPasswordRequest(email))) {
                is Resource.ERROR -> _uiState.update { LoginUiState.LoginFailed(result.errorCode) }
                is Resource.SUCCESS -> _uiState.update { LoginUiState.ForgetPasswordDone }
            }

        }
    }

    fun signupWithEmail(
        email: String,
        deviceId: String,
        instanceId: String,
        passwordHash: String
    ) {
        job {
            _uiState.update { LoginUiState.Loading }
            val result = authRepository.signUp(
                SignUpRequest(
                    email = email,
                    deviceId = deviceId,
                    instanceId = instanceId,
                    passwordHash = passwordHash

                )
            )
            when (result) {
                is Resource.ERROR -> _uiState.update { LoginUiState.LoginFailed(result.errorCode) }
                is Resource.SUCCESS -> _uiState.update { LoginUiState.SignupDone }
            }

        }
    }


    fun authoriseWithEmail(
        email: String,
        deviceId: String,
        instanceId: String,
        passwordHash: String
    ) {
        job {
            _uiState.update { LoginUiState.Loading }
            val result = authRepository.signIn(
                SignInRequest(
                    email = email,
                    deviceId = deviceId,
                    instanceId = instanceId,
                    passwordHash = passwordHash

                )
            )

            when (result) {
                is Resource.ERROR -> _uiState.update { LoginUiState.LoginFailed(result.errorCode) }
                is Resource.SUCCESS -> {

                    val uiState = result.data.data?.token?.let {
                        authorised(it)
                        LoginUiState.LoginSucceed
                    } ?: LoginUiState.LoginFailed()
                    _uiState.update { uiState }
                }
            }
        }

    }
}