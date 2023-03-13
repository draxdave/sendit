package com.drax.sendit.data.service.models

import androidx.annotation.IntDef


@IntDef(
    PushOp.OP_CONNECTION_DISCONNECTED,
    PushOp.OP_DEVICE_SIGNIN,
    PushOp.OP_DEVICE_SIGNOUT,
    PushOp.OP_NEW_CONTENT,
    PushOp.PUSH_OP_NEW_CONNECTION
)
@Retention(AnnotationRetention.SOURCE)
annotation class PushOp {

    companion object {
        const val PUSH_OP_NEW_CONNECTION = 500
        const val OP_CONNECTION_DISCONNECTED = 502
        const val OP_DEVICE_SIGNOUT = 503
        const val OP_NEW_CONTENT = 504
        const val OP_DEVICE_SIGNIN = 505
    }
}