package com.drax.sendit.data.model

import kotlinx.serialization.Serializable
import java.util.*


@Serializable
data class User (
    val id                   : String,
    val fullname             : String,
    val avatar               : String,
    val email                : String,
    val birthDate            : String,
    val phone                : String,
    val type                 : UserType,
    val isServiceEnabled     : Boolean,
    val language             : String
) {

    companion object {

        // App default language
        val appDefaultLocale: Locale = Locale.ENGLISH
        fun createGuestUser() = User(
            id = "",
            fullname = "Guest user",
            avatar = "https://sendit-app.s3.ap-southeast-1.amazonaws.com/public/basic_account_avatar.png",
            email = "",
            birthDate = "",
            phone = "",
            type = UserType.Guest,
            isServiceEnabled = false,
            language = appDefaultLocale.language)
    }
}

@Serializable
sealed class UserType {
    @Serializable object Guest : UserType()
    @Serializable object SignedIn : UserType()
    @Serializable object VIP : UserType()
}
