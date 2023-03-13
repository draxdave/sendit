package com.drax.sendit.data.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Long,
    val email: String
)
