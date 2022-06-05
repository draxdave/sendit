package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef

@IntDef(UserSex.UserSex_MALE, UserSex.UserSex_FEMAL, UserSex.UserSex_NONE)
@Retention(AnnotationRetention.SOURCE)
annotation class UserSex {

    companion object{
        const val UserSex_MALE = 200
        const val UserSex_FEMAL = 300
        const val UserSex_NONE = 400
    }
}