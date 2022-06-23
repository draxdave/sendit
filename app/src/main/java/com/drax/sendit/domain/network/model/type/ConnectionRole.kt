package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef

@IntDef(ConnectionRole.ROLE_CONNECTEE, ConnectionRole.ROLE_CONNECTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class ConnectionRole {

    companion object{
        const val ROLE_CONNECTEE = 100
        const val ROLE_CONNECTOR = 200
    }
}