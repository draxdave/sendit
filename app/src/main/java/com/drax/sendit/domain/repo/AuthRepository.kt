package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signInDevice(signInRequest: SignInRequest): Resource<ApiResponse<SignInResponse>>
    fun signOutDevice(): Flow<Resource<ApiResponse<Unit>>>
}