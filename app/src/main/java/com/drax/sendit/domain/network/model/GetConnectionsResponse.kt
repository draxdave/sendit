package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Transaction

data class GetConnectionsResponse(
    val connections: List<Connection>
)
