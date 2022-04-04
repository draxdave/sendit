package com.drax.sendit.data.db

import androidx.room.TypeConverter
import com.drax.sendit.data.model.InstantSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant


class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return value?.let{ Json.decodeFromString(InstantSerializer,value)}
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): String? {
        return date?.let { Json.encodeToString(InstantSerializer,date)}
    }
}