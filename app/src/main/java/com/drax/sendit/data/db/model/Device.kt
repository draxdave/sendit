package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.BuildConfig

@Entity(tableName = "devices")
data class Device (
    @PrimaryKey
    val id: Long,

    val instanceId: String,
    val name: String,
    val iconUrl: String,
    val isThisDevice: Boolean,
    val addedDate: Long,
    val platform: String,
    val platformVersion: String,
    val appVersion: Int,
    val userId: String
){
    companion object {
        fun thisDevice(name: String, instanceId: String, platform: String, platformVersion: String) = Device(
            id = 0,
            instanceId = instanceId,
            name = name,
            iconUrl = "https://sendit-app.s3.ap-southeast-1.amazonaws.com/public/android+(2).png",
            isThisDevice = true,
            addedDate = System.currentTimeMillis(),
            platform = platform,
            platformVersion = platformVersion,
            appVersion = BuildConfig.VERSION_CODE,
            userId = instanceId
        )
    }
}