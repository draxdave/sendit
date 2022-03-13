package com.drax.sendit.data.service

import android.content.Intent
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.view.util.DeviceInfoHelper
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
    private val devicesRepository: DevicesRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
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
        GlobalScope.launch(Dispatchers.IO) {
            devicesRepository.updateDevice(
                Device.thisDevice(
                    name = DeviceInfoHelper.model,
                    token = token
                )
            )
            registryRepository.setFirebaseId(token)
        }
    }

    companion object{
        const val INVITATION_RESPONSE = "invitation response event"

        fun token(then : (String) -> Unit)=
            run {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        println( "Fetching FCM registration token failed"+ task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    then(token)
                })
            }
    }
}

