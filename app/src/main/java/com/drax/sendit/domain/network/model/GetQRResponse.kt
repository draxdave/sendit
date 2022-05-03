package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetQRResponse(
    @SerialName("qr_url") val qrUrl: String
)
