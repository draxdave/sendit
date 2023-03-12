package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionModel (
    val id: Int,

    @SerialName("connect_date") val connectDate: Long,

    @SerialName("connector_id") val connectorId: Int,
    @SerialName("connectee_id") val connecteeId: Int,
    val status: Int,
    val type: Int,

    @SerialName("last_used") val lastUsed: Long,

    val meta: String
)