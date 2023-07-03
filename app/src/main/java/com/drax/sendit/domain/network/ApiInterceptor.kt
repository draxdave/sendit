package com.drax.sendit.domain.network

import android.util.Log
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.ErrorResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody

@Singleton
class ApiInterceptor @Inject constructor(
    private val json: Json,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain) = try {
        chain.proceed(chain.request().let {
            it.newBuilder()
                .url(
                    it.url.toString()
                        .replace(AppRetrofit.BaseUrl, AppRetrofit.BaseUrl + AppRetrofit.UrlVersion)
                        .toHttpUrlOrNull() ?: it.url
                )
                .build()
        })
    } catch (e: Exception) {
        Log.e("ApiInterceptor", "intercept: ", e)
        Response.Builder()
            .code(500)
            .message("Error while trying to connect to server")
            .body(ResponseBody.create("application/json".toMediaTypeOrNull(),
                ApiResponse<Unit>(
                    statusCode = 500,
                    error = ErrorResponse(
                        type = 500,
                        description = "Error while trying to connect to server"
                    )
                ).let { json.encodeToString(it) }
            ))
            .build()
    }
}