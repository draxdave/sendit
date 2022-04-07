package com.drax.sendit.domain.network

import com.google.gson.*
import org.json.JSONObject
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class InstantDeserializer : JsonDeserializer<Instant> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        if (json == null)
            return null
//"2022-04-01 23:31:35",

        return deserialize(json.asString)
    }
    fun deserialize(json: String): Instant = LocalDateTime.parse(
        json,
        DateTimeFormatter.ofPattern(
            if (json.contains("T")) TIMEZONE_DATE_FORMAT
            else COMMON_DATE_FORMAT

        ))
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

    fun serialize(instant: Instant): String{
        val formatter = DateTimeFormatter.ofPattern(TIMEZONE_DATE_FORMAT)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault())
        val instantStr = formatter.format(instant)
        return instantStr
    }

    companion object{
        private const val COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val TIMEZONE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
    }
}