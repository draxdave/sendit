package com.drax.sendit.data.service.models

import app.siamak.sendit.R
import kotlinx.serialization.Serializable

@Serializable
data class NotificationModel (
    val id                  : Int = System.currentTimeMillis().toInt(),
    val icon                : Int,
    val isSticky            : Boolean,
    val title               : Int,
    val text                : Int,
    val vibrate             : Boolean,
    val content             : String? = null,
    val data: NotificationData? = null
): java.io.Serializable{
    companion object {

        fun default(
            title: Int,
            text: String
        ) = NotificationModel(
            icon = R.drawable.ic_round_send_24,
            isSticky = false,
            title = title,
            text = 0,
            vibrate = true,
            content = text
        )

        fun newContent(
            title: Int,
            text: String,
            data: NotificationData
        ) = NotificationModel(
            icon = R.drawable.ic_round_send_24,
            isSticky = false,
            title = title,
            text = 0,
            vibrate = true,
            content = text,
            data = data
        )
    }
}

@Serializable
sealed class NotificationData: java.io.Serializable {
    data class Transaction( val transaction: com.drax.sendit.data.db.model.Transaction): NotificationData()
}