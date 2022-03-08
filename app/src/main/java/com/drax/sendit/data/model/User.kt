package com.drax.sendit.data.model

import java.util.*


data class User (
    var id                   : String = "",
    var fullname             : String = "",
    var avatar               : String = "",
    var email                : String = "",
    var birthDate            : String = "",
    var source               : String = "",
    var phone                : String = "",
    var emailVerifyAt        : String = "",
    var role                 : String = "",
    var password             : String = "",
    var isServiceEnabled              : Boolean = false,
    var language                      : String = appDefaultLocale.language
) {

    companion object {

        // App default language
        val appDefaultLocale: Locale = Locale.ENGLISH
    }
}


