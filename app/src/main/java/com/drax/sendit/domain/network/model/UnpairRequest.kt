package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import kotlinx.serialization.SerialName

data class UnpairRequest(
    @SerialName("connection_id") val connectionId: Long
)

data class UnpairResponse(
    val connection: Connection
)