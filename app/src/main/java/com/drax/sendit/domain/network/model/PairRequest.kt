package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PairRequest(
    @SerialName("request_code") val requestCode: String
)
