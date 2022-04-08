package com.drax.sendit.di.builder

import com.drax.sendit.domain.network.InstantDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.serialization.json.Json
import java.text.DateFormat
import java.time.Instant

class Json {
    fun build(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = true
        }
    }
}