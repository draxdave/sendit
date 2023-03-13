package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_BLOCKED

@IntDef(UserStatus_ACTIVE, UserStatus_BLOCKED)
@Retention(AnnotationRetention.SOURCE)
annotation class UserStatus {

    companion object {
        const val UserStatus_ACTIVE = 200
        const val UserStatus_BLOCKED = 301
    }
}