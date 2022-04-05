package com.drax.sendit.domain.network

import com.drax.sendit.data.db.AuthDao
import com.drax.sendit.domain.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor

class AuthInterceptor(
    private val authDao: AuthDao
): Interceptor {

    override fun intercept(chain: Interceptor.Chain) = chain.proceed(chain.request()).also {
        if (it.code == UNAUTHORIZED_ACCESS) {
            unAuthorizedAccessDetected()
        }
    }

    private fun unAuthorizedAccessDetected(){
        GlobalScope.launch(Dispatchers.Default) {
            authDao.clearDeviceData()
            authDao.clearUserData()
            authDao.clearHistoryData()
        }
    }

    companion object{
        const val UNAUTHORIZED_ACCESS = 401
    }
}