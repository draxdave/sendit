package com.drax.sendit.view

import com.drax.sendit.data.db.model.Device
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

data class DeviceWrapper(
    val device: Device
){
    val addedDate: String by lazy {
        getDateFromMilliseconds(device.addedDate)
    }

    private fun getDateFromMilliseconds(instant: Instant): String {
        val dateFormat = "dd MMMMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = instant.epochSecond
        return formatter.format(calendar.time)
    }
}