package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_BLOCKED
import com.drax.sendit.domain.network.model.type.DeviceStatus.Companion.DeviceStatus_SIGNOUT
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_BLOCKED

@IntDef(DeviceStatus_ACTIVE, DeviceStatus_BLOCKED, DeviceStatus_SIGNOUT)
@Retention(AnnotationRetention.SOURCE)
annotation class DeviceStatus {

    companion object{
        const val DeviceStatus_ACTIVE = 200
        const val DeviceStatus_BLOCKED = 301
        const val DeviceStatus_SIGNOUT = 401
    }
}