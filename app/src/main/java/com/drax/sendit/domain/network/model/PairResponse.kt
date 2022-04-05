package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Connection

data class PairResponse(
    val connection: Connection
){
    companion object{
        const val WAITING_FOR_PEER = 1322
        const val ALREADY_ACTIVE = 1321
        const val REJECTED = 1426
    }
}
