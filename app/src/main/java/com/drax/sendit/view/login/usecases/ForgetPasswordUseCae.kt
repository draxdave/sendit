package com.drax.sendit.view.login.usecases

import androidx.compose.runtime.MutableState
import app.siamak.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.model.toDrawableId
import com.drax.sendit.data.model.toStringId
import com.drax.sendit.domain.network.model.auth.ForgotPasswordRequest
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.view.login.FormState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForgetPasswordUseCae @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(formState: MutableState<FormState>, email: String) {
        formState.value = FormState.Loading
        when (val result = authRepository.forgotPassword(
            ForgotPasswordRequest(
                email = email,
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
                formState.value = FormState.Success()
            }
        }
    }
}