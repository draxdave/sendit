package com.drax.sendit.view.login

import kotlinx.serialization.Serializable

@Serializable
sealed class FormType : java.io.Serializable {
    object Login : FormType()
    object Register : FormType()
    object ForgetPassword : FormType()
}