package com.drax.sendit.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant


class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return value?.let{ Json.decodeFromString(value)}
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): String? {
        return date?.let { Json.encodeToString(date)}
    }
}