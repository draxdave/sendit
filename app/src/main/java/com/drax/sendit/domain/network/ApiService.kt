package com.drax.sendit.domain.network

import com.drax.sendit.domain.network.model.ApiResponse
import com.drax.sendit.domain.network.model.GetConnectionsResponse
import com.drax.sendit.domain.network.model.GetQRResponse
import com.drax.sendit.domain.network.model.GetTransactionsResponse
import com.drax.sendit.domain.network.model.PairRequest
import com.drax.sendit.domain.network.model.PairResponse
import com.drax.sendit.domain.network.model.PairResponseRequest
import com.drax.sendit.domain.network.model.PairResponseResponse
import com.drax.sendit.domain.network.model.ShareRequest
import com.drax.sendit.domain.network.model.ShareResponse
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.network.model.SignInResponse
import com.drax.sendit.domain.network.model.UnpairRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {


    @POST("/user/signin")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<ApiResponse<SignInResponse>>

    @POST("/device/pair")
    suspend fun pair(
        @Body request: PairRequest
    ): Response<ApiResponse<PairResponse>>


    @POST("/device/pair/response")
    suspend fun pairResponse(
        @Body request: PairResponseRequest
    ): Response<ApiResponse<PairResponseResponse>>

    @POST("/device/unpair")
    suspend fun unpair(
        @Body request: UnpairRequest
    ): Response<ApiResponse<Unit>>

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

    @GET("/device/pair/qr")
    suspend fun getQr(): Response<ApiResponse<GetQRResponse>>
}




