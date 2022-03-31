package com.drax.sendit.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant


class TimeConverters {
    companion object {

        @TypeConverter
        fun fromTimestamp(value: String?): Instant? {
            return Json.decodeFromString(value?:"")
        }

        @TypeConverter
        fun dateToTimestamp(date: Instant?): String? {
            return Json.encodeToString(date)
        }
    }
}