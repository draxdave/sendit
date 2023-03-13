package com.drax.sendit.data.db

import androidx.room.TypeConverter
import com.drax.sendit.data.model.InstantSerializer
import com.drax.sendit.di.builder.Json
import java.time.Instant


class TimeConverters {
    private val json = Json().build()

    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return value?.let { json.decodeFromString(InstantSerializer, value) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): String? {
        return date?.let { json.encodeToString(InstantSerializer, date) }
    }
}