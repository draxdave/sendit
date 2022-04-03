package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.User

data class SignInResponse(
    val token: String,
    val user: User,
    val connections: List<Connection>?,
    val device: Device
)

