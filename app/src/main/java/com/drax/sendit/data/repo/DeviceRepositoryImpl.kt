package com.drax.sendit.data.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.RegistryRepository
import kotlinx.coroutines.flow.flow


class DeviceRepositoryImpl(
    private val registryRepository: RegistryRepository

): DeviceRepository {
    override suspend fun addOrUpdateDevice(device: Device) = registryRepository.updateThisDevice(device)

    override fun getSelfDevice() = registryRepository.getThisDevice()

    override suspend fun clearDb() {
        registryRepository.updateThisDevice(null)
    }
}