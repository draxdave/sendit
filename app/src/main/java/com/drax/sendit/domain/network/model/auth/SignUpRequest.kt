package com.drax.sendit.domain.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("instance_id") val instanceId: String,
    @SerialName("password_hash") val passwordHash: String
)

