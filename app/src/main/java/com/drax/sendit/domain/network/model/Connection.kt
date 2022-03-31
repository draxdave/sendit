package com.drax.sendit.domain.network.model

import com.drax.sendit.domain.network.model.type.ConnectionStatus
import com.drax.sendit.domain.network.model.type.ConnectionType
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Connection(
    val id: Long,
    @SerializedName("connector_id") val connectorId: Long,
    @SerializedName("connectee_id") val connecteeId: Long,

    @SerializedName("connect_date") val connectDate: Instant,
    @ConnectionStatus val status: Int,
    @ConnectionType val type: Int,

    @SerializedName("last_used") val lastUsed: Instant,
    val meta: String
)