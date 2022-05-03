package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import kotlinx.serialization.Serializable

@Serializable
data class PairResponseResponse(
    val connection: Connection
)
