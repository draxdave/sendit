package com.drax.sendit.domain.network

import com.drax.sendit.domain.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor

class AuthInterceptor(
    private val authRepository: UserRepository,
): Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(chain.request()).also {
        if (it.code == 401) {
            unAuthorizedAccessDetected()
        }
    }

    private fun unAuthorizedAccessDetected(){
        GlobalScope.launch(Dispatchers.Default) {
            authRepository.clearDb()
        }
    }
}