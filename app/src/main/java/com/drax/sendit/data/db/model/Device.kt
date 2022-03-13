package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device (
    @PrimaryKey
    val id: Long,

    val instanceId: String,
    val name: String,
    val iconUrl: String,
    val isThisDevice: Boolean,
    val addedDate: Long = System.currentTimeMillis()
){
    companion object {
        fun thisDevice(name: String, instanceId: String) = Device(
            id = 0,
            instanceId = instanceId,
            name = name,
            iconUrl = "https://sendit-app.s3.ap-southeast-1.amazonaws.com/public/android+(2).png",
            isThisDevice = true
        )
    }
}