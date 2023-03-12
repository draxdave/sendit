package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.network.model.type.TransactionStatus
import com.drax.sendit.domain.network.model.type.TransactionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Transaction(
    @PrimaryKey(autoGenerate = true)  val iid: Long=0,

    val id: Long,
    @SerialName("broadcaster_id")
    val broadcasterId: Long,

    @SerialName("connection_id")
    val connectionId: Long,

    @SerialName("send_date") val sendDate: Long,

    @SerialName("deliver_date") val deliverDate: Long?,
    val content: String,
    @TransactionStatus val status: Int,

    @SerialName("last_update") val lastUpdate: Long,
    val meta: String,

    @TransactionContentType
    @SerialName("content_type")
    val contentType: Int,

    @TransactionType val type: Int,
): java.io.Serializable