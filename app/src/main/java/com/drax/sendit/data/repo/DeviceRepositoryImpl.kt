package com.drax.sendit.data.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.UpdateInstanceIdRequest
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.RegistryRepository


class DeviceRepositoryImpl(
    private val registryRepository: RegistryRepository,
    private val apiService: ApiService

) : DeviceRepository {
    override suspend fun addOrUpdateDevice(device: Device) =
        registryRepository.updateThisDevice(device)

    override fun getSelfDevice() = registryRepository.getThisDevice()

    override suspend fun clearDb() = registryRepository.updateThisDevice(null)

    override suspend fun storeToken(token: String) = registryRepository.updateToken(token)
    override suspend fun updateInstanceId(instanceId: String) {
        registryRepository.setFirebaseId(instanceId)
        apiService.updateInstanceId(
            UpdateInstanceIdRequest(instanceId)
        )
    }

    override fun getApiToken() = registryRepository.getApiToken()

    override suspend fun getQRUrlFromServer() = NetworkCall {
        apiService.getQr()
    }.fetch()

    override suspend fun storeQRUrl(qrUrl: String) = registryRepository.updateQrUrl(qrUrl)

    override fun getQrUrl() = registryRepository.getQrUrl()


}