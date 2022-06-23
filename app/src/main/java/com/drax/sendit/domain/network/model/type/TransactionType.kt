package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef

@IntDef(
    TransactionType.TransactionType_SYSTEM,
    TransactionType.TransactionType_NORMAL
)
@Retention(AnnotationRetention.SOURCE)
annotation class TransactionType {

    companion object{
        const val TransactionType_SYSTEM = 100
        const val TransactionType_NORMAL = 200
    }
}

