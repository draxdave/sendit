package com.drax.sendit.domain.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class InstantSerializer : JsonDeserializer<Instant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        if (json == null)
            return null
//"2022-04-01 23:31:35",

        return LocalDateTime.parse(
            json.asString,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)
        )                                      // Returns a `LocalDateTime` object.
            .atZone(                               // Apply a zone to that unzoned `LocalDateTime`, giving it meaning, determining a point on the timeline.
                ZoneId.systemDefault()     // Always specify a proper time zone with `Contintent/Region` format, never a 3-4 letter pseudo-zone such as `PST`, `CST`, or `IST`.
            )                                      // Returns a `ZonedDateTime`. `toString` → 2018-05-12T16:30-04:00[America/Toronto].
            .toInstant()
    }
}