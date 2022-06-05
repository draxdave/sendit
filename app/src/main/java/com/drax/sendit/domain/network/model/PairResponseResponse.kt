package com.drax.sendit.domain.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PairResponseResponse(
    val connection: ConnectionModel
)
