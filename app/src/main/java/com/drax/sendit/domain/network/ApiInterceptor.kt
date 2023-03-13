package com.drax.sendit.domain.network

import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor

@Singleton
class ApiInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(chain.request().let {
        it.newBuilder()
            .url(
                it.url.toString()
                    .replace(AppRetrofit.BaseUrl, AppRetrofit.BaseUrl + AppRetrofit.UrlVersion)
                    .toHttpUrlOrNull() ?: it.url
            )
            .build()
    })
}