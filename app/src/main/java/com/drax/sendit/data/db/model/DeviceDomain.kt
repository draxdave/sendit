package com.drax.sendit.data.db.model

import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.domain.network.model.type.DeviceStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDomain(
    val id: Long,
    val name: String,
    @DevicePlatform val platform: Int,
    @DeviceStatus val status: Int,
    val region: String,
    val meta: String,
    val model: String,
    @SerialName("icon_url") val iconUrl: String,
    @SerialName("added_date") val addedDate: Long,
    @SerialName("platform_version") val platformVersion: String,
    @SerialName("app_version") val appVersion: Int,
    @SerialName("language_code") val languageCode: String,
    @SerialName("last_touch") val lastTouch: Long,
    val isThisDevice: Boolean = false,
)