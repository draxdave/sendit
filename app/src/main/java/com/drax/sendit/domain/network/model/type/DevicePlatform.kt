package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_ANDROID
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_CHROME
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_IOS
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_WEB

@IntDef(
    DevicePlatform_ANDROID,
    DevicePlatform_IOS,
    DevicePlatform_WEB,
    DevicePlatform_CHROME
)
@Retention(AnnotationRetention.SOURCE)
annotation class DevicePlatform {

    companion object{
        const val DevicePlatform_ANDROID = 1
        const val DevicePlatform_IOS = 2
        const val DevicePlatform_WEB = 3
        const val DevicePlatform_CHROME = 4
    }
}