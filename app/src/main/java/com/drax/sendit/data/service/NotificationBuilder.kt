package com.drax.sendit.data.service

import com.drax.sendit.R
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.service.models.NotificationData
import com.drax.sendit.data.service.models.NotificationModel

class NotificationBuilder(
    private val notificationUtil: NotificationUtil
) {

    fun fireNotification(model: Any){
        when(model){
            is Transaction ->{
                notificationUtil.buildAndPop(NotificationModel.newContent(
                    title = R.string.notification_title_new_content,
                    text = model.content,
                    data = NotificationData.Transaction(model)
                ))
            }
            else -> Unit
        }
    }
}