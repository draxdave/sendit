package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnpairRequest(
    @SerialName("connection_id") val connectionId: Long
)

@Serializable
data class UnpairResponse(
    val connection: Connection
)