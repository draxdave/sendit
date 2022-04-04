package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.data.model.InstantSerializer
import com.drax.sendit.domain.network.model.type.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity(tableName = "connection")
@Serializable
data class Connection (
    @PrimaryKey
    val id: Long,

    @SerializedName("connect_date") @Serializable(with = InstantSerializer::class) val connectDate: Instant,
    @ConnectionStatus val status: Int,
    @ConnectionType val type: Int,

    @SerializedName("last_used") @Serializable(with = InstantSerializer::class) val lastUsed: Instant,
    val meta: String,
    val name: String,
    @SerializedName("icon_url") val iconUrl: String,
    @DevicePlatform val platform: Int,
    @SerializedName("platform_version") val platformVersion: String,
    @DeviceStatus @SerializedName("device_status") val deviceStatus: Int,
    val model: String,
    @ConnectionRole val role: Int,
)