package com.drax.sendit.data.repo

import com.drax.sendit.data.db.TransactionsDao
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.repo.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionsDao: TransactionsDao,
    private val apiService: ApiService
): TransactionRepository {
    override fun getAllTransactions() = transactionsDao.getList()

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