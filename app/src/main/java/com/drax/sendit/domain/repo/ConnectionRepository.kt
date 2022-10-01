package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.GetConnectionResponse
import com.drax.sendit.domain.network.model.GetConnectionsResponse
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.network.model.PairResponseRequest
import com.drax.sendit.domain.network.model.PairResponseResponse
import com.drax.sendit.domain.network.model.UnpairRequest
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository: BaseStorageRepository{
    fun getConnections(onlyActive: Boolean): Flow<List<Connection>>
    fun getConnectionsFromServer(): Flow<Resource<ApiResponse<GetConnectionsResponse>>>
    fun getConnectionFromServer(id: String): Flow<Resource<ApiResponse<GetConnectionResponse>>>
    suspend fun addConnection(vararg connection: Connection)
    suspend fun emptyConnections()

    fun sendPairRequest(pairRequest: PairRequest): Flow<Resource<ApiResponse<PairResponse>>>
    fun unpair(unpairRequest: UnpairRequest): Flow<Resource<ApiResponse<Unit>>>
}