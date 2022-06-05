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
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale


object DeviceInfoHelper {

    val platform = DevicePlatform.DevicePlatform_ANDROID

    val model = deviceModel

    val user: String = USER

    val deviceId: String = ID

    @SuppressLint("HardwareIds")
    fun getId(context: Context): String {
        val androidId = try {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e:Exception){
            ID
        }

//        return  md5(androidId)
        return  androidId

    }
    val platformVersion: Int = VERSION.SDK_INT

    val deviceModel
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

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}