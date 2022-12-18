package com.drax.sendit.domain.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateInstanceIdRequest(
    @SerialName("instance_id") val instanceId: String
)
