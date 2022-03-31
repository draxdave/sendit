package com.drax.sendit.view.qr

import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.ResViewModel

class QrVM(
    private val pushRepository: PushRepository,
    private val devicesRepository: DevicesRepository
): ResViewModel() {

//    fun sendInvitation(partyId:String): MutableLiveData<Resource<FirebaseSendResponse>> {
//        val result = MutableLiveData(Resource.loading<FirebaseSendResponse>(null))
//        job {
////            result.postValue(pushRepository.sendInvitation(partyId))
//        }
//        return result
//    }

//    fun addDevice(device: Device): MutableLiveData<Long?> {
//        val result = MutableLiveData<Long?>(null)
//        job {
//            result.postValue(devicesRepository.addDevice(device))
//        }
//        return result
//    }
}