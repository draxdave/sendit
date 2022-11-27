package com.drax.sendit.di.builder

import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Singleton
class Json {
    fun build(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
}