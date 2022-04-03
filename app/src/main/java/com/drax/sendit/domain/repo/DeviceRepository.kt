package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.*
import kotlinx.coroutines.flow.Flow

interface DeviceRepository: BaseStorageRepository{

    suspend fun addOrUpdateDevice(device: Device)

    fun getSelfDevice(): Device?
}