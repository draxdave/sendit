package com.drax.sendit.data.model

import com.drax.sendit.domain.network.model.type.UserSex
import com.drax.sendit.domain.network.model.type.UserStatus
import com.drax.sendit.domain.network.model.type.UserType
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.util.*


@Serializable
data class User (
    val id: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("full_name") val fullName: String,

    @SerializedName("last_login") @Serializable(with = InstantSerializer::class) val lastLogin: Instant,

    @SerializedName("birth_date") @Serializable(with = InstantSerializer::class) val birthDate: Instant,
    @SerializedName("avatar_url") val avatarUrl: String,
    val email: String,
    @UserSex val sex: Int,
    @UserType val type: Int,
    val language: String,
    @UserStatus val status: Int,
    val meta: String


){

    companion object {
        // App default language
        val appDefaultLocale: Locale = Locale.ENGLISH
    }
}

object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }
}