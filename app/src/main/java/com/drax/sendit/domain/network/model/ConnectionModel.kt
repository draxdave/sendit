package com.drax.sendit.domain.network.model

import com.drax.sendit.data.model.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ConnectionModel (
    val id: String,

    @SerialName("connect_date") @Serializable(with = InstantSerializer::class)
    val connectDate: Instant,

    @SerialName("connector_id") val connectorId: String,
    @SerialName("connectee_id") val connecteeId: String,
    val status: String,
    val type: String,

    @SerialName("last_used") @Serializable(with = InstantSerializer::class)
    val lastUsed: Instant,

    val meta: String
)