package com.drax.sendit.domain.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val type: Int,
    val description: String
)