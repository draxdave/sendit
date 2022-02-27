package com.drax.sendit.domain.network.model

data class Invitation(val message:String,val senderToken:String,val sent:Long,val type:String="invitation")
data class InvitationResponse(val message:String,val senderToken:String,val sent:Long,val type:String="accepted")

data class Content(val message:String,val senderToken:String,val sent:Long,val type:String="content")