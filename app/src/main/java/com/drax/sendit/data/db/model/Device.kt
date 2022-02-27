package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device (
    @PrimaryKey
    var instanceId: String,

    var name: String,
    var addedDate: Long = System.currentTimeMillis()
)