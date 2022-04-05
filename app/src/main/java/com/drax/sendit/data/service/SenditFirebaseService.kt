package com.drax.sendit.data.service

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.service.models.NewInvitation
import com.drax.sendit.data.service.models.PushOp
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject


class SenditFirebaseService : FirebaseMessagingService() {

    private val deviceRepository: DeviceRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("onMessageReceived")
        println(Gson().toJson(remoteMessage.data))
        if (remoteMessage.data.containsKey("op"))
            when(remoteMessage.data["op"]?.toInt()){
                null ->Unit

                PushOp.OP_NEW_CONNECTION_REQUEST -> remoteMessage.data["data"]?.let {
                    bundleOf("data" to Json.decodeFromString<NewInvitation>(it)).send()
                }

            }
    }

    private fun Bundle.send(){
        val intent = Intent(INVITATION_RESPONSE)
        intent.putExtras(this)
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
        const val INVITATION_RESPONSE = "invitation_response_event"

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

