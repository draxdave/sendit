package com.drax.sendit.data.repo

import com.drax.sendit.data.db.AuthDao
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.auth.ForgotPasswordRequest
import com.drax.sendit.domain.network.model.auth.SignUpRequest
import com.drax.sendit.domain.network.model.auth.signin.SignInRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.domain.repo.AuthRepository
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val authDao: AuthDao,
): AuthRepository {

    override suspend fun signInSso(signInSsoRequest: SignInSsoRequest) = NetworkCall {
        apiService.signInSso(signInSsoRequest)
    }.fetch()

    override suspend fun signIn(signInRequest: SignInRequest) = NetworkCall {
        apiService.signIn(signInRequest)
    }.fetch()

    override suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) = NetworkCall {
        apiService.forgotPassword(forgotPasswordRequest)
    }.fetch()

    override suspend fun signUp(signUpRequest: SignUpRequest) = NetworkCall {
        apiService.signUp(signUpRequest)
    }.fetch()

    override fun signOutDevice() = flow {

        NetworkCall {
            apiService.signOut()
        }.fetch()
            .also {
                emit(it)
                authDao.clearDeviceData()
                authDao.clearUserData()
                authDao.clearHistoryData()
            }
    }
}