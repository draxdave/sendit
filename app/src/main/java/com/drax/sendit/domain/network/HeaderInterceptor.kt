package com.drax.sendit.domain.network

import app.siamak.sendit.BuildConfig
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

@Singleton
class HeaderInterceptor @Inject constructor(
    private val registryRepository: RegistryRepository,
                                            private val deviceInfoHelper: DeviceInfoHelper,
): Interceptor {

    private val apiToken: String
        get() = registryRepository.getApiToken() ?: ""

    private val headers by lazy {
        Headers.headersOf(
            "lang" , "en",
            "app-version", BuildConfig.VERSION_CODE.toString(),
            "region","hk",
            "device-platform", deviceInfoHelper.platform.toString(),
            "device-platform-version", deviceInfoHelper.platformVersion.toString(),
            "device-model", deviceInfoHelper.model
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        requestBuilder.headers(headers)
        requestBuilder.addHeader("api-key", apiToken)
        return chain.proceed(requestBuilder.build())
    }
}