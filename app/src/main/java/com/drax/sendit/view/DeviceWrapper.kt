package com.drax.sendit.view

import com.drax.sendit.data.db.model.Device
import java.text.SimpleDateFormat
import java.util.*

data class DeviceWrapper(
    val device: Device
){
    val addedDate: String by lazy {
        getDateFromMilliseconds(device.addedDate)
    }

    private fun getDateFromMilliseconds(millis: Long): String {
        val dateFormat = "dd MMMMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }
}