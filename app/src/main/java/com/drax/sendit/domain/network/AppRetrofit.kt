package com.drax.sendit.domain.network

import com.drax.sendit.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppRetrofit(
    private val authInterceptor: AuthInterceptor,
    private val headerInterceptor: HeaderInterceptor,
    private val errorHandlerInterceptor: ErrorHandlerInterceptor,
    private val json: Json
) {


    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .client(buildClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun buildClient():OkHttpClient{
        return OkHttpClient.Builder().apply {
            connectTimeout(120, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            readTimeout(120, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(headerInterceptor)
            addInterceptor(errorHandlerInterceptor)
            addInterceptor(authInterceptor)

            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })


        }.build()


    }
}

