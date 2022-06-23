package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.data.model.InstantSerializer
import com.drax.sendit.domain.network.model.type.ConnectionRole
import com.drax.sendit.domain.network.model.type.ConnectionStatus
import com.drax.sendit.domain.network.model.type.ConnectionType
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DeviceStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity(tableName = "connection")
@Serializable
data class Connection (
    @PrimaryKey
    val id: Long,

    @SerialName("connect_date") @Serializable(with = InstantSerializer::class) val connectDate: Instant,
    @ConnectionStatus val status: Int,
    @ConnectionType val type: Int,

    @SerialName("last_used") @Serializable(with = InstantSerializer::class) val lastUsed: Instant,
    val meta: String,
    val name: String,
    @SerialName("icon_url") val iconUrl: String,
    @DevicePlatform val platform: Int,
    @SerialName("platform_version") val platformVersion: String,
    @DeviceStatus @SerialName("device_status") val deviceStatus: Int,
    val model: String,
    @ConnectionRole val role: Int,
)