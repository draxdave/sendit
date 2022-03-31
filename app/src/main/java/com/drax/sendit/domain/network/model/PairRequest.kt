package com.drax.sendit.domain.network.model

import com.google.gson.annotations.SerializedName

data class PairRequest(
    @SerializedName("device_id") val deviceId: Long
)
