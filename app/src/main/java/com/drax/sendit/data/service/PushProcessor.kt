package com.drax.sendit.data.service

import android.os.Bundle
import androidx.core.os.bundleOf
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.service.models.NewInvitation
import com.drax.sendit.data.service.models.PushOp
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

@Singleton
class PushProcessor @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val connectionRepository: ConnectionRepository,
    private val notificationBuilder: NotificationBuilder,
    private val json: Json,
    private val analytics: Analytics
){

    fun process(op: String?, rawData: String?): Pair<String, Bundle>? = when(op?.toInt()){
        null -> null

        PushOp.PUSH_OP_NEW_CONNECTION -> {
            analytics.set(Event.Notification.ConnectionRequest)
            rawData?.let {
                val data = json.decodeFromString<NewInvitation>(it)
                tryStoreNewConnection(data.connectionId)
                NEW_CONNECTION to bundleOf("data" to data)
            }
        }

        PushOp.OP_NEW_CONTENT -> {
            analytics.set(Event.Notification.Content)
            rawData?.let {
                processNewContent(json.decodeFromString(it))
                null
            }
        }

        else -> null
    }

    private fun tryStoreNewConnection(connectionId: String) {
        job {
            connectionRepository.getConnectionFromServer(connectionId).collect { result ->
                if (result is Resource.SUCCESS) {
                    connectionRepository.addConnection(
                        result.data.data?.connection
                            ?: return@collect
                    )
                }
            }
        }
    }

    private fun processNewContent(transaction: Transaction) = try {
        job {
            // Store
            transactionRepository.insertNewTransaction(transaction)
        }
        // Notify User
        notificationBuilder.fireNotification(transaction)
        // Update server to delivered
    } catch (e: Exception){
        e.printStackTrace()
    }

    private fun job(dispatcher: CoroutineContext = Dispatchers.IO, job: suspend CoroutineScope.() -> Unit){
        GlobalScope.launch(dispatcher, CoroutineStart.DEFAULT, job)
    }

    companion object {
        const val NEW_CONNECTION = "new_connection_event"
    }
}
