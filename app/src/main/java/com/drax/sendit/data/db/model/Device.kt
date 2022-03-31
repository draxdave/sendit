package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.drax.sendit.data.db.TimeConverters
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DeviceStatus
import com.google.gson.annotations.SerializedName
import java.time.Instant

@Entity(tableName = "device")
data class Device (
    @PrimaryKey
    val iid: Long,

    val id: Long,
    val name: String,
    val uid: String,
    val token: String,
    @DevicePlatform val platform: Int,
    @DeviceStatus val status: Int,
    val region: String,
    val meta: String,
    val model: String,
    @SerializedName("instance_id") val instanceId: String,
    @SerializedName("icon_url") val iconUrl: String,

    @SerializedName("added_date") val addedDate: Instant,
    @SerializedName("platform_version") val platformVersion: String,
    @SerializedName("app_version") val appVersion: Int,
    @SerializedName("user_id") val userId: String,
    @SerializedName("language_code") val languageCode: String,


    @SerializedName("last_touch") val lastTouch: Instant,
    val isThisDevice: Boolean,
)