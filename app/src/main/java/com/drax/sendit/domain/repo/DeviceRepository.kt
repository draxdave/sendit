package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.GetQRResponse
import com.drax.sendit.domain.network.model.device.WhoisResponse
import kotlinx.coroutines.flow.Flow

interface DeviceRepository : BaseStorageRepository {
    val deviceUniqueId: String
    val deviceInstanceId: String

    suspend fun addOrUpdateDevice(device: DeviceDomain)
    fun getSelfDevice(): Flow<DeviceDomain?>
    suspend fun storeToken(token: String)
    suspend fun updateInstanceId(instanceId: String)
    fun getApiToken(): String?
    suspend fun getQRUrlFromServer(): Resource<ApiResponse<GetQRResponse>>

    suspend fun storeQRUrl(qrUrl: String)
    fun getQrUrl(): Flow<String?>

    suspend fun getWhois(): Resource<ApiResponse<WhoisResponse>>

    suspend fun pushNewInstanceId(id: String)
}
