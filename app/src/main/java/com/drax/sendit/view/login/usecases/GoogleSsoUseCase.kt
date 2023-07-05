package com.drax.sendit.view.login.usecases

import androidx.compose.runtime.MutableState
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.toDrawableId
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoResponse
import com.drax.sendit.domain.network.model.device.WhoisModel
import com.drax.sendit.domain.network.model.device.toWhoisModel
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.login.FormState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleSsoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository,
    private val analytics: Analytics,
) {
    suspend operator fun invoke(
        formState: MutableState<FormState>,
        username: String,
        idToken: String
    ) {
        val deviceId = deviceRepository.deviceUniqueId
        val instanceId = deviceRepository.deviceInstanceId

        formState.value = FormState.Loading

        when (val result = authRepository.signInSso(
            SignInSsoRequest(
                email = username,
                deviceId = deviceId,
                instanceId = instanceId,
                tokenId = idToken,
            )
        )) {
            is Resource.ERROR -> formState.value = result.toFormState()

            is Resource.SUCCESS -> {
                authorised(formState = formState, token = result.data.data?.token ?: "")
                formState.value = FormState.Success()
            }
        }
    }

    private suspend fun authorised(token: String, formState: MutableState<FormState>) {
        deviceRepository.storeToken(token)

        val (user, device) = getWhois(formState = formState) ?: return
        userRepository.addOrUpdateUser(user)
        deviceRepository.addOrUpdateDevice(device)
        analytics.set(Event.SignIn.GoToHome)
    }


    private suspend fun getWhois(formState: MutableState<FormState>): WhoisModel? =
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

}

internal fun Resource.ERROR.toFormState() = FormState.Error(
    messageResId = if (errorCode in 1..799) {
        errorCode.toStringId()
    } else when (errorCode) {
        SignInSsoResponse.DEVICE_IS_NOT_ACTIVE -> R.string.login_error_device_inactive
        SignInSsoResponse.INCORRECT_CREDENTIALS -> R.string.login_error_user_pass_incorrect
        SignInSsoResponse.USER_IS_NOT_ACTIVE -> R.string.login_error_user_inactive
        SignInSsoResponse.USER_ALREADY_ACTIVE -> R.string.login_error_user_inactive
        else -> R.string.error_internal
    },
    iconResId = if (errorCode in 1..799) {
        errorCode.toDrawableId()
    } else {
        R.drawable.warning
    },
)
