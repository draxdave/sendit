package com.drax.sendit.data.service

import android.content.Intent
import android.os.Bundle
import com.drax.sendit.BuildConfig
import com.drax.sendit.domain.repo.DeviceRepository
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

    private val pushProcessor: PushProcessor by inject()
    private val deviceRepository: DeviceRepository by inject()


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("onMessageReceived")
        println(Gson().toJson(remoteMessage.data))
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

    @Suppress("EXPERIMENTAL_API_USAGE")
    private fun updateDeviceInstanceId(instanceId: String) {
        GlobalScope.launch(Dispatchers.IO) {
            deviceRepository.storeInstanceId(instanceId)
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

