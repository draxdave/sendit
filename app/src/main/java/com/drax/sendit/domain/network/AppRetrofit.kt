package com.drax.sendit.domain.network

import com.drax.sendit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AppRetrofit(private val authInterceptor: AuthInterceptor ) {


    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .client(buildClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buildClient():OkHttpClient{
        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)

            if (BuildConfig.DEBUG)
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })


            addInterceptor {
                val original: Request = it.request()

                val request: Request = original.newBuilder()
                    .header("Authorization", "key=${BuildConfig.F_API_KEY}}")
                    .method(original.method, original.body)
                    .build()

                it.proceed(request)
            }
            addInterceptor(authInterceptor)
        }.build()


    }
}

