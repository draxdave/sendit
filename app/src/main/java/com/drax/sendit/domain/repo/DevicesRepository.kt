package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.*
import kotlinx.coroutines.flow.Flow

interface DevicesRepository: LocalStorageRepository{
    fun sendInvitation(pairRequest: PairRequest): Flow<Resource<ApiResponse<PairResponse>>>
    fun invitationResponse(pairResponseRequest: PairResponseRequest): Flow<Resource<ApiResponse<PairResponseResponse>>>
    fun unpair(unpairRequest: UnpairRequest): Flow<Resource<ApiResponse<UnpairResponse>>>
    suspend fun addDevice(devices: Device)

    fun getAllDevices(): Flow<List<Device>>
}