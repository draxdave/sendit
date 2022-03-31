package com.drax.sendit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.domain.network.model.type.UserSex
import com.drax.sendit.domain.network.model.type.UserStatus
import com.drax.sendit.domain.network.model.type.UserType
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*


@Entity
data class User (
    @PrimaryKey val id: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("full_name") val fullName: String,

    @SerializedName("last_login") val lastLogin: Instant,

    @SerializedName("birth_date") val birthDate: Instant,
    @SerializedName("avatar_url") val avatarUrl: String,
    val email: String,
    @UserSex val sex: Int,
    @UserType val type: Int,
    val language: String,
    @UserStatus val status: Int,
    val meta: String


) {

    companion object {
        // App default language
        val appDefaultLocale: Locale = Locale.ENGLISH
    }
}