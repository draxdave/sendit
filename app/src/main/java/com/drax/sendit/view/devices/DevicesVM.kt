package com.drax.sendit.view.devices

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DevicesVM @Inject constructor(
    private val devicesRepository: DevicesRepository,
    private val pushRepository: PushRepository
) : ViewModel() {

    val devices: LiveData<List<Device>> = devicesRepository.getAllDevices()
        .asLiveData()

    fun removeDevice(unpairRequest: UnpairRequest){
        job {
            devicesRepository.unpair(unpairRequest)
        }
    }
}