package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_ANDROID
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_CHROME
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_IOS
import com.drax.sendit.domain.network.model.type.DevicePlatform.Companion.DevicePlatform_WEB
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_BLOCKED
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_SIGNOUT
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_BLOCKED

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