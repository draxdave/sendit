package com.drax.sendit.view.login

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
sealed class FormType : java.io.Serializable {
    object Login : FormType()
    object Register : FormType()
    object ForgetPassword : FormType()
}

@Serializable
sealed class FormState : java.io.Serializable {
    object Loading : FormState()
    data class Error(
        val message: String? = null,
        @StringRes val messageResId: Int? = null,
        val iconResId: Int
    ) :
        FormState()

    object Valid : FormState()
    object Invalid : FormState()
    data class Success(@StringRes val messageRes: Int? = null) : FormState()
}