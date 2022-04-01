package com.drax.sendit.domain.network.model

import com.drax.sendit.domain.network.model.type.UserSex
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class SignInRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    @UserSex val sex: Int,

    @SerializedName("birth_date") val birthDate: Instant,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("device_id") val deviceId: String,
    @SerializedName("instance_id") val instanceId: String,
    @SerializedName("id_token") val tokenId: String
)