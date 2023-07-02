package com.drax.sendit.view.login.usecases

import androidx.compose.runtime.MutableState
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.toDrawableId
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.auth.SignUpRequest
import com.drax.sendit.domain.network.model.auth.signin.SignInRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoResponse
import com.drax.sendit.domain.network.model.device.WhoisModel
import com.drax.sendit.domain.network.model.device.toWhoisModel
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.login.FormState
import com.drax.sendit.view.util.md5
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val analytics: Analytics,
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(
        formState: MutableState<FormState>,
        username: String,
        password: String,
        passwordRepeat: String,
    ) {
        if (password != passwordRepeat) {
            formState.value = FormState.Error(
                messageResId = R.string.passwords_dont_match, iconResId = R.drawable.warning
            )
            return
        }

        val deviceId = deviceRepository.deviceUniqueId
        val instanceId = deviceRepository.deviceInstanceId

        formState.value = FormState.Loading

        when (val result = authRepository.signUp(
            SignUpRequest(
                email = username,
                passwordHash = password.md5(),
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
                formState.value = FormState.Success(R.string.signup_done)
            }
        }
    }


}