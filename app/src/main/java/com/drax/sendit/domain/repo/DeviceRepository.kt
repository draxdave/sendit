package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.GetQRResponse
import kotlinx.coroutines.flow.Flow

interface DeviceRepository: BaseStorageRepository{

    suspend fun addOrUpdateDevice(device: Device)
    fun getSelfDevice(): Flow<Device?>
    suspend fun storeToken(token: String)
    suspend fun storeInstanceId(instanceId: String)
    fun getApiToken(): String?
    suspend fun getQRUrlFromServer(): Resource<ApiResponse<GetQRResponse>>

    suspend fun storeQRUrl(qrUrl: String)
    fun getQrUrl(): Flow<String?>
}