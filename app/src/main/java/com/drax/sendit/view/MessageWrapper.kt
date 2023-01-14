package com.drax.sendit.view

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.db.model.Transaction
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Locale

data class MessageWrapper(
    val message: Transaction,

    val thisDevice: DeviceDomain?,
    val connection: Connection?,
    val isSender: Boolean
){
    val addedDate: String by lazy {
        getDateFromMilliseconds(message.sendDate)
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