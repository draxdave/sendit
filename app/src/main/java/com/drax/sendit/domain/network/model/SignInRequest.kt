package com.drax.sendit.domain.network.model

import com.drax.sendit.domain.network.model.type.UserSex
import kotlinx.serialization.SerialName
import java.time.Instant

data class SignInRequest(
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("full_name") val fullName: String,
    val email: String,
    @UserSex val sex: Int,

    @SerialName("birth_date") val birthDate: Instant,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("instance_id") val instanceId: String,
    @SerialName("id_token") val tokenId: String
)