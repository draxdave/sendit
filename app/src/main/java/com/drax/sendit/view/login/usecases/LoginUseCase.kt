package com.drax.sendit.view.login.usecases

import androidx.compose.runtime.MutableState
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.toDrawableId
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
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
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val analytics: Analytics,
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(
        formState: MutableState<FormState>,
        username: String,
        password: String
    ) {
        val deviceId = deviceRepository.deviceUniqueId
        val instanceId = deviceRepository.deviceInstanceId

        formState.value = FormState.Loading

        when (val result = authRepository.signIn(
            SignInRequest(
                email = username,
                passwordHash = password.md5(),
                deviceId = deviceId,
                instanceId = instanceId,
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