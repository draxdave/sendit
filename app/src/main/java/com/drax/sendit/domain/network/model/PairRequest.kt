package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName

data class PairRequest(
    @SerialName("request_code") val requestCode: String
)
