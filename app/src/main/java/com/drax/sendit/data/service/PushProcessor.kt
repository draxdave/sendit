package com.drax.sendit.data.service

import androidx.core.os.bundleOf
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.service.models.NewInvitation
import com.drax.sendit.data.service.models.PushOp
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

class PushProcessor (
    private val deviceRepository: DeviceRepository,
    private val transactionRepository: TransactionRepository,
    private val notificationBuilder: NotificationBuilder,
    private val json: Json
){

    fun process(op: String?, data: String?) = when(op?.toInt()){
        null -> null

        PushOp.OP_NEW_CONNECTION_REQUEST -> data?.let {
            INVITATION_RESPONSE to bundleOf("data" to json.decodeFromString<NewInvitation>(it))
        }

        PushOp.OP_NEW_CONTENT -> data?.let {
            processNewContent(json.decodeFromString(it))
            null
        }

        else -> null
    }

    private fun processNewContent(transaction: Transaction) = try {
        job {
            // Store
            transactionRepository.insertNewTransaction(transaction)
        }
        // Notify User
        notificationBuilder.fireNotification(transaction)
        // Update server to delivered
    }catch (e: Exception){
            e.printStackTrace()
    }

    companion object {
        const val INVITATION_RESPONSE = "invitation_response_event"
    }

    private fun job(dispatcher: CoroutineContext = Dispatchers.IO, job: suspend CoroutineScope.() -> Unit){
        GlobalScope.launch(dispatcher, CoroutineStart.DEFAULT, job)
    }
}