package com.drax.sendit.domain.network

import com.drax.sendit.BuildConfig
import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

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
            json.asString ,
            DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" , Locale.US )
        )                                      // Returns a `LocalDateTime` object.
            .atZone(                               // Apply a zone to that unzoned `LocalDateTime`, giving it meaning, determining a point on the timeline.
                ZoneId.systemDefault()     // Always specify a proper time zone with `Contintent/Region` format, never a 3-4 letter pseudo-zone such as `PST`, `CST`, or `IST`.
            )                                      // Returns a `ZonedDateTime`. `toString` → 2018-05-12T16:30-04:00[America/Toronto].
            .toInstant()
    }
}

class AppRetrofit(
    private val authInterceptor: AuthInterceptor,
    private val headerInterceptor: HeaderInterceptor,
    private val errorHandlerInterceptor: ErrorHandlerInterceptor,
) {


    fun getRetrofitClient(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java , InstantSerializer())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setDateFormat(DateFormat.FULL)
            .setPrettyPrinting()
            .setVersion(1.0)
            .create()

        return Retrofit.Builder()
            .client(buildClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun buildClient():OkHttpClient{
        return OkHttpClient.Builder().apply {
            connectTimeout(120, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            readTimeout(120, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

            addInterceptor(headerInterceptor)
            addInterceptor(errorHandlerInterceptor)
            addInterceptor(authInterceptor)
            /* addInterceptor {
            val original: Request = it.request()

            val request: Request = original.newBuilder()
                .header("Authorization", "key=${BuildConfig.F_API_KEY}}")
                .method(original.method, original.body)
                .build()

            it.proceed(request)
        }*/

        }.build()


    }
}

