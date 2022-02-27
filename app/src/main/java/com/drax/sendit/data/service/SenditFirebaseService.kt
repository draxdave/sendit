package com.drax.sendit.data.service

import android.content.Intent
import com.drax.sendit.data.repo.RegistryRepository
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

