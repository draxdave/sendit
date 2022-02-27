package com.drax.sendit.data.model

import java.util.*

data class User (
    var id                   : String = "",
    var fullname             : String = "",
    var avatar               : String = "",
    var email                : String = "",
    var birthDate            : String = "",
    var gender               : String = Gender.Male,
    var source               : String = "",
    var phone                : String = "",
    var emailVerifyAt        : String = "",
    var role                 : String = "",
    var password             : String = "",
    var isVoiceRecordingEnabled       : Boolean = true,
    var isServiceEnabled              : Boolean = false,
    var isPhotoEnabled                : Boolean = false,
    var isLocationEnabled             : Boolean = false,
    var language                      : String = appDefaultLocale.language
) {

    companion object {

        // App default language
        val appDefaultLocale:Locale=Locale.ENGLISH
        object Gender {
            final const val Male = "male"
            final const val Female = "female"
        }
    }
}


