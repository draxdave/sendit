package com.drax.sendit.domain.network.model

import com.google.gson.annotations.SerializedName

data class PairRequest(
    @SerializedName("request_code") val requestCode: String
)
