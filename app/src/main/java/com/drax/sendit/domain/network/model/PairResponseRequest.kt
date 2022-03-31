package com.drax.sendit.domain.network.model

import com.google.gson.annotations.SerializedName

data class PairResponseRequest(
    @SerializedName("connection_id") val connectionId: Long,
    val response: Int
)
