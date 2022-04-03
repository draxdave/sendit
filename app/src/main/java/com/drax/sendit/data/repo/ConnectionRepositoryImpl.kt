package com.drax.sendit.data.repo

import com.drax.sendit.data.db.ConnectionDao
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponseRequest
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import kotlinx.coroutines.flow.flow

class ConnectionRepositoryImpl(
    private val apiService: ApiService,
    private val connectionDao: ConnectionDao,
)
    : ConnectionRepository {
    override fun getConnections() = connectionDao.getList()

    override suspend fun addConnection(vararg connection: Connection) = connectionDao.add(*connection)

    override fun sendInvitation(pairRequest: PairRequest) = flow {
        emit(
            NetworkCall {
                apiService.pair(pairRequest)
            }.fetch()
        )
    }

    override fun invitationResponse(pairResponseRequest: PairResponseRequest) = flow {
        emit(
            NetworkCall {
                apiService.pairResponse(pairResponseRequest)
            }.fetch()
        )
    }

    override fun unpair(unpairRequest: UnpairRequest) = flow {
        emit(
            NetworkCall {
                apiService.unpair(unpairRequest)
            }.fetch()
        )
    }

    override suspend fun clearDb() = connectionDao.deleteAll()

}