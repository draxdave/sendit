package com.drax.sendit.data.service.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewInvitation(
    @SerialName("connection_id") val connectionId: String,
    @SerialName("device_name") val deviceName: String,
) : java.io.Serializable