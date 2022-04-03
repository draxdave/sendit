package com.drax.sendit.data.service

import android.content.Intent
import com.drax.sendit.domain.repo.RegistryRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class SenditFirebaseService : FirebaseMessagingService() {

    private val registryRepository: RegistryRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("onMessageReceived")
        println(Gson().toJson(remoteMessage.data))
        when(remoteMessage.data["type"]){
            "accepted" -> applicationContext.sendBroadcast(Intent(INVITATION_RESPONSE).apply {
                putExtra("message",remoteMessage.data["message"])
                putExtra("senderToken",remoteMessage.data["senderToken"])
            })

        }


    }

    override fun onNewToken(token: String) {
        println("Firebase registrationToken=$token FCM")
        updateDeviceInstanceId(token)
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun updateDeviceInstanceId(instanceId: String) {
        GlobalScope.launch(Dispatchers.IO) {

//            try{
//                val thisDevice: Device = registryRepository.i()
//                    .first { it.isThisDevice }
//                devicesRepository.updateDevice( thisDevice.copy(instanceId = instanceId))
//
//            }catch (e: NoSuchElementException){
//                devicesRepository.addDevice(
//                    Device.thisDevice(
//                        name = DeviceInfoHelper.model,
//                        instanceId = instanceId,
//                        platform = "Android",
//                        platformVersion = DeviceInfoHelper.platformVersion.toString()
//                    )
//                )
//            }

            registryRepository.setFirebaseId(instanceId)
        }
    }

    companion object{
        const val INVITATION_RESPONSE = "invitation response event"

        fun token(onError : () -> Unit, then : (String) -> Unit) =
            run {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        println( "Fetching FCM registration token failed"+ task.exception)
                        onError()
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    then(token)
                })
            }
    }
}

