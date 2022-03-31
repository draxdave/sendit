package com.drax.sendit.domain.network.model

import com.drax.sendit.data.db.model.Transaction

data class GetTransactionsResponse(
    val page: Int,
    val transactions: List<Transaction>
)
