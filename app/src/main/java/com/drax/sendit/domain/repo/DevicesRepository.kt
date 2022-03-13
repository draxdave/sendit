package com.drax.sendit.domain.repo

import androidx.lifecycle.LiveData
import com.drax.sendit.data.db.model.Device
import kotlinx.coroutines.flow.Flow

interface DevicesRepository{
    fun addDevice(device: Device):Long
    fun removeDevice(deviceId: Long)
    fun updateDevice(device: Device)
    fun getAllDevices(): Flow<List<Device>>
    fun getAllDevicesSync():List<Device>
}