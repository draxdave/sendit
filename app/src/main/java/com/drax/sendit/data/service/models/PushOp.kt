package com.drax.sendit.data.service.models

import androidx.annotation.IntDef


@IntDef(
    PushOp.OP_CONNECTION_DISCONNECTED,
    PushOp.OP_DEVICE_SIGNIN,
    PushOp.OP_DEVICE_SIGNOUT,
    PushOp.OP_NEW_CONNECTION_REQUEST,
    PushOp.OP_NEW_CONTENT,
    PushOp.OP_REQUESTEE_RESPONDED)
@Retention(AnnotationRetention.SOURCE)
annotation class PushOp {

    companion object{
        const val OP_REQUESTEE_RESPONDED = 500
        const val OP_NEW_CONNECTION_REQUEST = 501
        const val OP_CONNECTION_DISCONNECTED = 502
        const val OP_DEVICE_SIGNOUT = 503
        const val OP_NEW_CONTENT = 504
        const val OP_DEVICE_SIGNIN = 505
    }
}