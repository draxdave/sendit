package com.drax.sendit.domain.network.model.device

import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class WhoisResponse(
    val user: User,
    val device: DeviceDomain,
)

data class WhoisModel(
    val user: User,
    val device: DeviceDomain,
)

fun WhoisResponse.toWhoisModel() = WhoisModel(
    user = user,
    device = device.copy(isThisDevice = true)
)