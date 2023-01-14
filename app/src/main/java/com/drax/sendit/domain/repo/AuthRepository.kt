package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.auth.ForgotPasswordRequest
import com.drax.sendit.domain.network.model.auth.SignUpRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoRequest
import com.drax.sendit.domain.network.model.auth.sso.SignInSsoResponse
import com.drax.sendit.domain.network.model.auth.signin.SignInRequest
import com.drax.sendit.domain.network.model.auth.signin.SignInResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signInSso(signInSsoRequest: SignInSsoRequest): Resource<ApiResponse<SignInSsoResponse>>
    suspend fun signIn(signInRequest: SignInRequest): Resource<ApiResponse<SignInResponse>>
    suspend fun signUp(signUpRequest: SignUpRequest): Resource<ApiResponse<Unit>>
    suspend fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): Resource<ApiResponse<Unit>>
    fun signOutDevice(): Flow<Resource<ApiResponse<Unit>>>
}