package com.drax.sendit.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.drax.sendit.data.service.models.NotificationModel
import com.drax.sendit.view.main.MainActivity
import com.google.gson.Gson

class NotificationUtil(
    private val context: Context
){
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    init {
        createNotificationChannel()
    }

    fun buildAndPop(notificationModel: NotificationModel){
        val notification = buildNotification(notificationModel)
        popNotification(notification, notificationModel.id)
    }

    private fun buildNotification(notificationModel: NotificationModel): Notification {
        val description = notificationModel.content ?: context.getString(notificationModel.text)
        val title = context.getString(notificationModel.title)

        val pendingIntent = PendingIntent.getActivity(context,0,
            Intent(context, MainActivity::class.java).apply {
                putExtra(ITEM,Gson().toJson(notificationModel))
            },
            PendingIntent.FLAG_UPDATE_CURRENT )


        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(notificationModel.icon)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//            .setAutoCancel(true)
//            .setTimeoutAfter(10000)
            .setOngoing(notificationModel.isSticky)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun popNotification(notification: Notification, id: Int) {

        //we give each notification the ID of the event it's describing,
        //to ensure they all show up and there are no duplicates
        notificationManager.notify(id , notification)
    }

    fun pullNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            mChannel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object{
        const val ITEM = "notif_service_object"
        private const val CHANNEL_NAME = "Sendit Main Notification Channel"
        private val CHANNEL_DESCRIPTION = "Sendit general notifications channel. You will not see new contents notifications if you mute this channel."
        private val CHANNEL_ID = "SenditMain"

    }
}