package com.drax.sendit.di.builder

import kotlinx.serialization.json.Json

class Json {
    fun build(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = true
        }
    }
}