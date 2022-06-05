package com.drax.sendit.domain.network

import com.drax.sendit.data.db.AuthDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor

class AuthInterceptor(
    private val authDao: AuthDao,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(chain.request()).also {
        if (it.code == UNAUTHORIZED_ACCESS) {
            unAuthorizedAccessDetected()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun unAuthorizedAccessDetected(){

        GlobalScope.launch(defaultDispatcher) {
            authDao.clearDeviceData()
            authDao.clearUserData()
            authDao.clearHistoryData()
        }
    }

    companion object{
        const val UNAUTHORIZED_ACCESS = 401
    }
}