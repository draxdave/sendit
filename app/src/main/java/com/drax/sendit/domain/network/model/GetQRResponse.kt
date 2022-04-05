package com.drax.sendit.domain.network.model

import com.google.gson.annotations.SerializedName

data class GetQRResponse(
    @SerializedName("qr_url") val qrUrl: String
)
