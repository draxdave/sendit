package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registry")
data class Registry(

    @PrimaryKey(autoGenerate = false)
    var key: String,
    var value: String?,
    var modifiedAt: Long = System.currentTimeMillis()
)