package com.drax.sendit.data.service

import android.content.Intent
import android.os.Bundle
import app.siamak.sendit.BuildConfig
import com.drax.sendit.domain.repo.DeviceRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SenditFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var pushProcessor: PushProcessor
    @Inject
    lateinit var deviceRepository: DeviceRepository
    @Inject
    lateinit var analytics: Analytics


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("onMessageReceived")
        analytics.set(Event.Notification.Any(remoteMessage.data))
        if (remoteMessage.data.containsKey("op") &&
            remoteMessage.data.containsKey("data"))
            pushProcessor.process(remoteMessage.data["op"], remoteMessage.data["data"])?.send()
    }

    private fun Pair<String,Bundle>.send(){
        val intent = Intent(first)
        intent.putExtras(second)
        applicationContext.sendBroadcast(intent)
    }

    override fun onNewToken(token: String) {
        println("Firebase registrationToken=$token FCM")
        updateDeviceInstanceId(token)
    }

    private fun updateDeviceInstanceId(instanceId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            deviceRepository.updateInstanceId(instanceId)
        }
    }

    companion object{

        fun token(onError : () -> Unit, then : (String) -> Unit) =
            run {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        if (BuildConfig.DEBUG)
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

