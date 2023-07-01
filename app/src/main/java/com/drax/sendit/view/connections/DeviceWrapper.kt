package com.drax.sendit.view.connections

import com.drax.sendit.data.db.model.Connection
import formatToDate

data class DeviceWrapper(
    val connection: Connection
) {
    val addedDate = connection.connectDate.formatToDate(dateFormat)

    companion object {
        private const val dateFormat = "dd MMMMM yyyy HH:mm"
    }
}
