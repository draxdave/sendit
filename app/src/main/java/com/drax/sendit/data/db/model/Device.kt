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
    val addedDate: Long = System.currentTimeMillis()
)