package com.drax.sendit.data.repo

import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.UpdateInstanceIdRequest
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val registryRepository: RegistryRepository,
    private val apiService: ApiService

) : DeviceRepository {
    override val deviceUniqueId: String by lazy {
        registryRepository.getDeviceId()
    }

    override val deviceInstanceId: String by lazy {
        registryRepository.getFirebaseId() ?: ""
    }

    override suspend fun addOrUpdateDevice(device: DeviceDomain){
        registryRepository.updateThisDevice(device)
    }

    override fun getSelfDevice() = registryRepository.getThisDevice()

    override suspend fun clearDb(): Unit {
        registryRepository.updateThisDevice(null)
    }

    override suspend fun storeToken(token: String){
        registryRepository.updateToken(token)
    }
    override suspend fun updateInstanceId(instanceId: String) {
        registryRepository.setFirebaseId(instanceId)
    }

    override suspend fun pushNewInstanceId(id: String){
        apiService.updateInstanceId(
            UpdateInstanceIdRequest(id)
        )
    }

    override fun getApiToken() = registryRepository.getApiToken()

    override suspend fun getQRUrlFromServer() = NetworkCall {
        apiService.getQr()
    }.fetch()

    override suspend fun storeQRUrl(qrUrl: String) {
        registryRepository.updateQrUrl(qrUrl)
    }

    override fun getQrUrl() = registryRepository.getQrUrl()

    override suspend fun getWhois() = NetworkCall {
        apiService.getWhois()
    }.fetch()
}