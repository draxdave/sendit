package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnpairRequest(
    @SerialName("connection_id") val connectionId: Long
)