package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_BLOCKED

@IntDef(
    ConnectionStatus.ConnectionStatus_ACTIVE,
    ConnectionStatus.ConnectionStatus_BLOCKED,
    ConnectionStatus.ConnectionStatus_DISCONNECTED,
    ConnectionStatus.ConnectionStatus_EXPIRED,
    ConnectionStatus.ConnectionStatus_PENDING,
    ConnectionStatus.ConnectionStatus_REJECTED
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConnectionStatus {

    companion object{
        const val ConnectionStatus_ACTIVE = 200
        const val ConnectionStatus_PENDING = 301
        const val ConnectionStatus_DISCONNECTED = 302
        const val ConnectionStatus_EXPIRED = 303
        const val ConnectionStatus_BLOCKED = 304
        const val ConnectionStatus_REJECTED = 305
    }
}