package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.network.model.FirebaseSendResponse

interface PushRepository {
    suspend fun sendInvitation(destinationId:String): Resource<FirebaseSendResponse>?
    suspend fun sendContent(content:String,destinationIds: List<String>):Resource<FirebaseSendResponse>?
    suspend fun sendContentToAll(content:String):Resource<FirebaseSendResponse>?

}

