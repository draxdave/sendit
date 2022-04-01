package com.drax.sendit.domain.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val statusCode: Int,
    val data: T ? = null,
    val error: ErrorResponse
){
    companion object{
        const val API_ERROR_BAD_REQUEST = 400
    }
}