package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef

@IntDef(
    TransactionContentType.TransactionType_CONTENT_MEDIA_IMAGE,
    TransactionContentType.TransactionType_CONTENT_TEXT,
    TransactionContentType.TransactionType_CONTENT_URL
)
@Retention(AnnotationRetention.SOURCE)
annotation class TransactionContentType {

    companion object{
        const val TransactionType_CONTENT_TEXT = 100
        const val TransactionType_CONTENT_URL = 200
        const val TransactionType_CONTENT_MEDIA_IMAGE = 300
    }
}

