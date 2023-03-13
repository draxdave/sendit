package com.drax.sendit.view

import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.db.model.Transaction
import formatToDate

data class MessageWrapper(
    val message: Transaction,

    val thisDevice: DeviceDomain?,
    val connection: Connection?,
    val isSender: Boolean
) {
    val addedDate = message.sendDate.formatToDate(dateFormat)

    companion object {
        private const val dateFormat = "MMMM dd HH:mm"
    }
}