package com.drax.sendit.domain.network.model.auth.sso

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInSsoRequest(
    val email: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("instance_id") val instanceId: String,
    @SerialName("id_token") val tokenId: String
)