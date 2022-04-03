package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import com.google.gson.annotations.SerializedName

data class UnpairRequest(
    @SerializedName("connection_id") val connectionId: Long
)

data class UnpairResponse(
    val connection: Connection
)