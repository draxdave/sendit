package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName

data class GetQRResponse(
    @SerialName("qr_url") val qrUrl: String
)
