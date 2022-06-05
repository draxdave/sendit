package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef

@IntDef(
    ConnectionType.ConnectionType_NORMAL,
    ConnectionType.ConnectionType_ONE_WAY_CONNECTEE,
    ConnectionType.ConnectionType_ONE_WAY_CONNECTOR,
    ConnectionType.ConnectionType_BLOCKED
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConnectionType {

    companion object{
        const val ConnectionType_NORMAL = 201
        const val ConnectionType_ONE_WAY_CONNECTEE = 301
        const val ConnectionType_ONE_WAY_CONNECTOR = 401
        const val ConnectionType_BLOCKED = 501
    }
}