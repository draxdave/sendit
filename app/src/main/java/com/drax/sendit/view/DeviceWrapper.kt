package com.drax.sendit.view

import com.drax.sendit.data.db.model.Connection
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

data class DeviceWrapper(
    val connection: Connection
){
    val addedDate: String by lazy {
        getDateFromMilliseconds(connection.connectDate)
    }

    private fun getDateFromMilliseconds(instant: Instant): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = instant.epochSecond
        return formatter.format(calendar.time)
    }
    
    companion object{
        private const val dateFormat = "dd MMMMM yyyy HH:mm"
    }
}