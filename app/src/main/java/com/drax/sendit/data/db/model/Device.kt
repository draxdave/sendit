package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.data.model.InstantSerializer
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DeviceStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Device (
    val id: Long,
    val name: String,
    val uid: String,
    val token: String,
    @DevicePlatform val platform: Int,
    @DeviceStatus val status: Int,
    val region: String,
    val meta: String,
    val model: String,
    @SerialName("instance_id") val instanceId: String,
    @SerialName("icon_url") val iconUrl: String,

    @SerialName("added_date") @Serializable(with = InstantSerializer::class) val addedDate: Instant,
    @SerialName("platform_version") val platformVersion: String,
    @SerialName("app_version") val appVersion: Int,
    @SerialName("user_id") val userId: String,
    @SerialName("language_code") val languageCode: String,


    @SerialName("last_touch") @Serializable(with = InstantSerializer::class) val lastTouch: Instant,
    val isThisDevice: Boolean,
)