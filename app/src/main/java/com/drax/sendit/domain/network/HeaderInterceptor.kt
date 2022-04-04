package com.drax.sendit.domain.network

import com.drax.sendit.BuildConfig
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(
    private val deviceRepository: DeviceRepository,
): Interceptor {

    private val apiToken: String
        get() = deviceRepository.getApiToken() ?: ""

    private val headers by lazy {
        Headers.headersOf(
            "lang" , "en",
            "app-version", BuildConfig.VERSION_CODE.toString(),
            "region","hk",
            "device-platform", DeviceInfoHelper.platform.toString(),
            "device-platform-version", DeviceInfoHelper.platformVersion.toString(),
            "device-model", DeviceInfoHelper.model
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.headers(headers)
        requestBuilder.addHeader("api-key", apiToken)
        return chain.proceed(requestBuilder.build())
    }
}