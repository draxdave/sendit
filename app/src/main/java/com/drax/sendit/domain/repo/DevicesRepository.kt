package com.drax.sendit.domain.repo

import androidx.lifecycle.LiveData
import com.drax.sendit.data.db.model.Device

interface DevicesRepository{
    suspend fun addDevice(device: Device):Long
    suspend fun removeDevice(deviceId: String)
    suspend fun updateDevice(device: Device)
    fun getAllDevices(): LiveData<List<Device>>
    fun getAllDevicesSync():List<Device>
}