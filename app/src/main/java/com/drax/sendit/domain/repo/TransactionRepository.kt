package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.GetTransactionsResponse
import kotlinx.coroutines.flow.Flow

interface TransactionRepository : BaseStorageRepository {

    fun getAllTransactions(): Flow<List<Transaction>>
    fun getAllTransactionsFromServer(): Flow<Resource<ApiResponse<GetTransactionsResponse>>>
    suspend fun insertNewTransaction(transaction: Transaction)

    fun removeLocally(transaction: Transaction)
}

