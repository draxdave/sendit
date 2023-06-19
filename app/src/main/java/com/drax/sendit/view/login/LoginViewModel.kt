package com.drax.sendit.view.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import app.siamak.sendit.R
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.User
import com.drax.sendit.data.model.toDrawableId
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.auth.ForgotPasswordRequest
import com.drax.sendit.domain.network.model.auth.signin.SignInRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoResponse
import com.drax.sendit.domain.network.model.device.WhoisModel
import com.drax.sendit.domain.network.model.device.toWhoisModel
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.job
import com.drax.sendit.view.util.md5
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val analytics: Analytics,
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    val formState = mutableStateOf<FormState>(FormState.Invalid)
    val formType = mutableStateOf<FormType>(FormType.Login)
    val usernameInput = mutableStateOf("")
    val passwordInput = mutableStateOf("")
    val passwordRepeatInput = mutableStateOf("")

    fun register(){
        job {
            if (passwordInput.value != passwordRepeatInput.value) {
                formState.value = FormState.Error(
                    messageResId = R.string.passwords_dont_match, iconResId = R.drawable.warning
                )
                return@job
            }
        }
    }

    fun loginWithEmail() {
        job {
            val deviceId = deviceRepository.deviceUniqueId
            val instanceId = deviceRepository.deviceInstanceId

            formState.value = FormState.Loading

            when (val result = authRepository.signIn(
                SignInRequest(
                    email = usernameInput.value,
                    passwordHash = passwordInput.value.md5(),
                    deviceId = deviceId,
                    instanceId = instanceId,
                )
            )) {
                is Resource.ERROR -> {
                    formState.value = FormState.Error(
                        messageResId = if (result.errorCode in 1..799) {
                            result.errorCode.toStringId()
                        } else when (result.errorCode) {
                            SignInSsoResponse.DEVICE_IS_NOT_ACTIVE -> R.string.login_error_device_inactive
                            SignInSsoResponse.INCORRECT_CREDENTIALS -> R.string.login_error_user_pass_incorrect
                            SignInSsoResponse.USER_IS_NOT_ACTIVE -> R.string.login_error_user_inactive
                            SignInSsoResponse.USER_ALREADY_ACTIVE -> R.string.login_error_user_inactive
                            else -> R.string.error_internal
                        },
                        iconResId = if (result.errorCode in 1..799) {
                            result.errorCode.toDrawableId()
                        } else {
                            R.drawable.warning
                        },
                    )
                }

                is Resource.SUCCESS -> {
                    authorised(result.data.data?.token ?: "")
                    formState.value = FormState.Success
                }
            }
        }
    }


    private suspend fun authorised(token: String) {
        deviceRepository.storeToken(token)

        val (user, device) = getWhois() ?: return
        userRepository.addOrUpdateUser(user)
        deviceRepository.addOrUpdateDevice(device)
        analytics.set(Event.SignIn.GoToHome)
    }

    fun forgetPassword() {
        job {
            formState.value = FormState.Loading
            when (val result = authRepository.forgotPassword(
                ForgotPasswordRequest(
                    email = usernameInput.value,
                )
            )) {
                is Resource.ERROR -> {
                    formState.value = FormState.Error(
                        messageResId = if (result.errorCode in 1..799) {
                            result.errorCode.toStringId()
                        } else {
                            R.string.error_internal
                        },
                        iconResId = if (result.errorCode in 1..799) {
                            result.errorCode.toDrawableId()
                        } else {
                            R.drawable.warning
                        },
                    )
                }

                is Resource.SUCCESS -> {
                    formState.value = FormState.Success
                }
            }
        }
    }

    private suspend fun getWhois(): WhoisModel? =
        when (val result = deviceRepository.getWhois()) {
            is Resource.ERROR -> {
                formState.value = FormState.Error(
                    messageResId = R.string.error_internal,
                    iconResId = R.drawable.warning
                )
                null
            }

            is Resource.SUCCESS -> result.data.data?.toWhoisModel()
        }


    fun validateInput(usernameInput: String, pattern: Pattern): Boolean {
        return usernameInput.matches(pattern.toRegex())
    }
}
