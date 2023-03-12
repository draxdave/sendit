package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val token: String,
    val user: User,
    val connections: List<Connection>?,
    val device: DeviceDomain
){
    companion object{
        const val DEVICE_IS_NOT_ACTIVE = 1805
        const val USER_IS_NOT_ACTIVE = 1804
    }
}

