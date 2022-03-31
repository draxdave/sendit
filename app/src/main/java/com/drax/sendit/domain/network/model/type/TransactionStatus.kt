package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_DELETED
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_DELIVERED
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_FAILED
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_OPENNED
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_RESEND
import com.drax.sendit.domain.network.model.type.TransactionStatus.Companion.TransactionStatus_SENT
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_ACTIVE
import com.drax.sendit.domain.network.model.type.UserStatus.Companion.UserStatus_BLOCKED

@IntDef(
    TransactionStatus_DELETED,
    TransactionStatus_DELIVERED,
    TransactionStatus_FAILED,
    TransactionStatus_OPENNED,
    TransactionStatus_RESEND,
    TransactionStatus_SENT
)
@Retention(AnnotationRetention.SOURCE)
annotation class TransactionStatus {

    companion object{
        const val TransactionStatus_SENT = 200
        const val TransactionStatus_DELIVERED = 201
        const val TransactionStatus_FAILED = 301
        const val TransactionStatus_OPENNED = 202
        const val TransactionStatus_RESEND = 203
        const val TransactionStatus_DELETED = 100

    }
}
