package com.drax.sendit.data.service.models

import com.drax.sendit.R

data class NotificationModel (
    val id                  : Int = System.currentTimeMillis().toInt(),
    val icon                : Int,
    val isSticky            : Boolean,
    val title               : Int,
    val text                : Int,
    val vibrate             : Boolean,
    val content             : String? = null
){
    companion object {
        fun Default(
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
    }
}
