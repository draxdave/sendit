package com.drax.sendit.view

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.db.model.Transaction
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Locale

data class TransactionWrapper(
    val transaction: Transaction,

    val thisDevice: Device?,
    val connection: Connection?,
    val isSender: Boolean
){
    val addedDate: String by lazy {
        getDateFromMilliseconds(transaction.sendDate)
    }

    private fun getDateFromMilliseconds(instant: Instant): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = instant.epochSecond
        return formatter.format(calendar.time)
    }

    companion object{
        private const val dateFormat = "MMMM dd HH:mm"
    }
}