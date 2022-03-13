package com.drax.sendit.view.util

import android.annotation.SuppressLint
import android.os.Build.*

object DeviceInfoHelper {

    val model = deviceModel

    val user: String? = USER

    val id: String? = ID

    private val deviceModel
        @SuppressLint("DefaultLocale")
        get() = capitalize(
            if (MODEL.toLowerCase().startsWith(MANUFACTURER.toLowerCase())) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            })


    private fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) toUpperCase() }
        }
    }

}