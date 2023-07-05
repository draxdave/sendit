package com.drax.sendit.data.service

import app.siamak.sendit.R
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.service.models.NotificationData
import com.drax.sendit.data.service.models.NotificationModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationBuilder @Inject constructor(
    private val notificationUtil: NotificationUtil
) {

    fun fireNotification(model: Any) {
        when (model) {
            is Transaction -> {
                notificationUtil.buildAndPop(
                    NotificationModel.newContent(
                        title = R.string.notification_title_new_content,
                        text = model.content,
                        data = NotificationData.Transaction(model)
                    )
                )
            }
            else -> Unit
        }
    }
}