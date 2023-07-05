package com.drax.sendit.view.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.ID
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.USER
import android.os.Build.VERSION
import android.provider.Settings
import com.drax.sendit.domain.network.model.type.DevicePlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class DeviceInfoHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val platform = DevicePlatform.DevicePlatform_ANDROID

    val model = deviceModel

    val user: String = USER

    val deviceId: String = ID

    @SuppressLint("HardwareIds")
    fun getId(): String {
        val androidId = try {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            deviceId
        }

//        return  md5(androidId)
        return androidId

    }

    val platformVersion: Int = VERSION.SDK_INT

    val deviceModel
        get() = capitalize(
            if (MODEL.lowercase(Locale.getDefault())
                    .startsWith(MANUFACTURER.lowercase(Locale.getDefault()))
            ) {
                MODEL
            } else {
                "$MANUFACTURER $MODEL"
            }
        )


    private fun capitalize(str: String) = str.apply {
        if (isNotEmpty()) {
            first().run { if (isLowerCase()) uppercaseChar() }
        }
    }
}
