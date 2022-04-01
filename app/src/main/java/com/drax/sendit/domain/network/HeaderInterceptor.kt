package com.drax.sendit.domain.network

import com.drax.sendit.BuildConfig
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor(
    private val registryRepository: RegistryRepository,
): Interceptor {

    private val apiToken: String? by lazy {
        registryRepository.getApiToken()
    }
    private val headers by lazy {
        Headers.headersOf(
            "lang" , "en",
            "app-version", BuildConfig.VERSION_CODE.toString(),
            "region","hk",
            "device-platform", DeviceInfoHelper.platform.toString(),
            "device-platform-version", DeviceInfoHelper.platformVersion.toString(),
            "device-model", DeviceInfoHelper.model,
            "api-key", apiToken ?: ""
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.headers(headers)
        return chain.proceed(requestBuilder.build())
    }
}