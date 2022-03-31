package com.drax.sendit.view.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.*
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat.getSystemService
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


object DeviceInfoHelper {

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

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}