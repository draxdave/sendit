package com.drax.sendit.domain.network.model

import com.drax.sendit.domain.network.model.type.PairResponseType
import com.google.gson.annotations.SerializedName

data class PairResponseRequest(
    @SerializedName("connection_id") val connectionId: Long,
    @PairResponseType val response: Int
)
