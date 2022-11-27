package com.drax.sendit.data.repo

import com.drax.sendit.data.db.TransactionsDao
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.repo.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow

@Singleton
class TransactionRepositoryImpl @Inject constructor(
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

    override fun removeLocally(transaction: Transaction) = transactionsDao.delete(transaction)

    override fun getAllTransactionsFromServer() = flow {
        emit(
            NetworkCall{
                apiService.getTransactions(1)
            }.fetch()
        )
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