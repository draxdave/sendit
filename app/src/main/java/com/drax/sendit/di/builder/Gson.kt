package com.drax.sendit.di.builder

import com.drax.sendit.domain.network.InstantDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.DateFormat
import java.time.Instant

class Gson {
    fun build(): Gson{
        return GsonBuilder()
            .registerTypeAdapter(Instant::class.java , InstantDeserializer())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.FULL)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()
    }
}