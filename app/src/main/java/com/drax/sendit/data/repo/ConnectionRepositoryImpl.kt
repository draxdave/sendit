package com.drax.sendit.data.repo

import com.drax.sendit.data.db.ConnectionDao
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.flow

@Singleton
class ConnectionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val connectionDao: ConnectionDao,
) : ConnectionRepository {
    override fun getConnections(onlyActive: Boolean) =
        if (onlyActive) connectionDao.getActiveConnections()
        else connectionDao.getAll()

    override suspend fun addConnection(vararg connection: Connection) =
        connectionDao.add(*connection)

    override suspend fun emptyConnections(): Unit = connectionDao.deleteAll()

    override fun sendPairRequest(pairRequest: PairRequest) = flow {
        emit(
            NetworkCall {
                apiService.pair(pairRequest)
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

    override fun getConnectionsFromServer() = flow {
        emit(
            NetworkCall {
                apiService.getConnections()
            }.fetch()
        )
    }

    override fun getConnectionFromServer(id: String) = flow {
        emit(
            NetworkCall {
                apiService.getConnection(id)
            }.fetch()
        )
    }
}