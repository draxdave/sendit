package com.drax.sendit.domain.network.model

data class FirebaseSendResponse (
    val multicast_id:String,
    val success:Int,
    val failure:Int,
    val canonical_ids:Int,
    val results:List<FirebaseSendResult>
    )
data class FirebaseSendResult(val message_id:String)


data class FirebaseSendRequest<T>(val registration_ids:List<String>, val collapse_key:String, val data:T)
