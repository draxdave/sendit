package com.drax.sendit.view.devices

import androidx.lifecycle.*
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.FirebaseSendResponse
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DevicesVM(
    private val devicesRepository: DevicesRepository,
    private val pushRepository: PushRepository
) : ResViewModel() {

    val devices: LiveData<List<Device>> = devicesRepository.getAllDevices()
        .asLiveData()

    fun removeDevice(unpairRequest: UnpairRequest){
        job {
            devicesRepository.unpair(unpairRequest)
        }
    }
}

fun ViewModel.job(dispatcher: CoroutineContext = Dispatchers.IO, job: suspend CoroutineScope.() -> Unit){
    viewModelScope.launch(dispatcher,CoroutineStart.DEFAULT, job)
}