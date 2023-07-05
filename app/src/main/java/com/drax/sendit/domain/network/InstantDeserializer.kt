package com.drax.sendit.domain.network

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class InstantDeserializer {

    fun deserialize(json: String): Instant = LocalDateTime.parse(
        json,
        DateTimeFormatter.ofPattern(
            if (json.contains("T")) TIMEZONE_DATE_FORMAT
            else COMMON_DATE_FORMAT

        )
    )
        .atZone(ZoneId.systemDefault())
        .toInstant()

//
//    override fun serialize(src: Instant?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
//
//        return src?.let {
//            JsonObject().get(serialize(src))
//        }?: JsonObject()
//
//    }

    fun serialize(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern(TIMEZONE_DATE_FORMAT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }

    companion object {
        private const val COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val TIMEZONE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}