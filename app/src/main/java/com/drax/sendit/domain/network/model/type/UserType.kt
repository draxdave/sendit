package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.UserType.Companion.UserType_NORMAL
import com.drax.sendit.domain.network.model.type.UserType.Companion.UserType_VIP

@IntDef(UserType_NORMAL, UserType_VIP)
@Retention(AnnotationRetention.SOURCE)
annotation class UserType {

    companion object{
        const val UserType_NORMAL = 200
        const val UserType_VIP = 301
    }
}