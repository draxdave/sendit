package com.drax.sendit.domain.network

import com.drax.sendit.domain.network.model.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @POST("/user/signin")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<ApiResponse<SignInResponse>>

    @POST("/device/pair")
    fun pair(
        @Body request: PairRequest
    ): Response<ApiResponse<PairResponse>>


    @POST("/device/pair/response")
    suspend fun pairResponse(
        @Body request: PairResponseRequest
    ): Response<ApiResponse<PairResponseResponse>>

    @POST("/device/unpair")
    suspend fun unpair(
        @Body request: UnpairRequest
    ): Response<ApiResponse<UnpairResponse>>

    @POST("/device/signout")
    suspend fun signOut(): Response<ApiResponse<Unit>>


    @POST("/share")
    suspend fun share(
        @Body request: ShareRequest
    ): Response<ApiResponse<ShareResponse>>

    @GET("/transactions")
    suspend fun getTransactions(
        @Query("page") page: Int
    ): Response<ApiResponse<GetTransactionsResponse>>

    @GET("/connections")
    suspend fun getConnections(): Response<ApiResponse<GetConnectionsResponse>>

    @GET("/pair/qr")
    suspend fun getQr(): Response<ApiResponse<GetQRResponse>>
}




