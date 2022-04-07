package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository: BaseStorageRepository {

    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun insertNewTransaction(transaction: Transaction)
}

