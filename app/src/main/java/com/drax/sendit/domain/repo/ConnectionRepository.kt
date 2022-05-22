package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.*
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository: BaseStorageRepository{
    fun getConnections(): Flow<List<Connection>>
    fun getConnectionsFromServer(): Flow<Resource<ApiResponse<GetConnectionsResponse>>>
    suspend fun addConnection(vararg connection: Connection)
    suspend fun emptyConnections()

    fun sendInvitation(pairRequest: PairRequest): Flow<Resource<ApiResponse<PairResponse>>>
    fun invitationResponse(pairResponseRequest: PairResponseRequest): Flow<Resource<ApiResponse<PairResponseResponse>>>
    fun unpair(unpairRequest: UnpairRequest): Flow<Resource<ApiResponse<UnpairResponse>>>
}