package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.google.gson.annotations.SerializedName

data class ShareRequest(
    @SerializedName("connection_id") val connectionId: Long,
    val content: String,
    @TransactionContentType val type: Int
)

data class ShareResponse(
    val transaction: Transaction
)