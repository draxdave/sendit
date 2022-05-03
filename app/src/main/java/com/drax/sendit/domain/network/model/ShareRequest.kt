package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.domain.network.model.type.TransactionContentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareRequest(
    @SerialName("connection_id") val connectionId: Long,
    val content: String,
    @TransactionContentType val type: Int
)

@Serializable
data class ShareResponse(
    val transaction: Transaction
)