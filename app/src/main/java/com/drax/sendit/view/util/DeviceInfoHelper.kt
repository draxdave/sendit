package com.drax.sendit.view.util

import android.annotation.SuppressLint
import android.os.Build.*
import java.util.*

object DeviceInfoHelper {

    val model = deviceModel

    val user: String? = USER

    val id: String? = ID

    private val deviceModel
        get() = capitalize(
            if (MODEL.lowercase(Locale.getDefault()).startsWith(MANUFACTURER.lowercase(Locale.getDefault()))) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            })


    private fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) uppercaseChar() }
        }
    }

}