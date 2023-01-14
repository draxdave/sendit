package com.drax.sendit.data.model

import java.util.Locale
import kotlinx.serialization.Serializable


@Serializable
data class User (
    val id: String,
    val email: String
){

    companion object {
        // App default language
        val appDefaultLocale: Locale = Locale.ENGLISH
    }
}