package com.drax.sendit.domain.network

import com.drax.sendit.domain.network.model.Content
import com.drax.sendit.domain.network.model.FirebaseSendRequest
import com.drax.sendit.domain.network.model.FirebaseSendResponse
import com.drax.sendit.domain.network.model.Invitation
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    @POST("/fcm/send")
    suspend fun sendInvitation(
        @Body request: FirebaseSendRequest<Invitation>
    ): Response<FirebaseSendResponse>



    @POST("/fcm/send")
    suspend fun sendContent(
        @Body request: FirebaseSendRequest<Content>
    ): Response<FirebaseSendResponse>
}




