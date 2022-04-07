package com.drax.sendit.data.repo

import com.drax.sendit.data.db.TransactionsDao
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.repo.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionsDao: TransactionsDao,
    private val apiService: ApiService
): TransactionRepository {
    override fun getAllTransactions() = transactionsDao.getList()
    override suspend fun clearDb() {
        transactionsDao.deleteAll()
    }

    override suspend fun insertNewTransaction(transaction: Transaction) {
        transactionsDao.add(transaction)
    }

    /*
    emit(
            object : NetworkCall<ApiResponse<PairResponse>>() {
                override suspend fun createCall(): Response<ApiResponse<PairResponse>> {
                    return apiService.pair(pairRequest)
                }
            }.fetch()
        )
     */
}