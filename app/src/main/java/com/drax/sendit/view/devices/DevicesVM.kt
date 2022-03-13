package com.drax.sendit.view.devices

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.FirebaseSendResponse
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DevicesVM(private val devicesRepository: DevicesRepository, private val pushRepository: PushRepository) : ResViewModel() {


    val permissionGranted = MutableLiveData(false)
    val serviceStarted =permissionGranted.map {
        it
    }
    val stateMessage = serviceStarted.map {
        if (it) {
            R.string.service_started
        } else {
            R.string.service_warning_permissions
        }
    }

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices:StateFlow<List<Device>> = _devices

    init {
        viewModelScope.launch(Dispatchers.Default) {
            devicesRepository.getAllDevices().collect {
                _devices.value = it
            }
        }
    }

    fun removeDevice(id:Long){
        async{ devicesRepository.removeDevice(id) }
    }

    private fun async(job: suspend ()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            job()
        }
    }

    fun sendInvitation(partyId:String):MutableLiveData<Resource<FirebaseSendResponse>>{
        val result = MutableLiveData(Resource.loading<FirebaseSendResponse>(null))
        async {
            result.postValue(pushRepository.sendInvitation(partyId))
        }
        return result
    }

    fun addDevice(device: Device): MutableLiveData<Long?> {
        val result = MutableLiveData<Long?>(null)
        async {
            result.postValue(devicesRepository.addDevice(device))
        }
        return result
    }
}