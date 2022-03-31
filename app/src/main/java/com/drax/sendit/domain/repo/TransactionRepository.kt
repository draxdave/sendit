package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository: LocalStorageRepository {

    fun getAllTransactions(): Flow<List<Transaction>>
}

