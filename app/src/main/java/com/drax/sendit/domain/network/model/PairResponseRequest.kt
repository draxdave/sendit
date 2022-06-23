package com.drax.sendit.domain.network.model

import com.drax.sendit.domain.network.model.type.PairResponseType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PairResponseRequest(
    @SerialName("connection_id") val connectionId: Long,
    @PairResponseType val response: Int
)
