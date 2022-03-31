package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.network.model.type.TransactionStatus
import com.drax.sendit.domain.network.model.type.TransactionType
import com.google.gson.annotations.SerializedName
import java.time.Instant

@Entity
data class Transaction(
    @PrimaryKey val iid: Long,

    val id: Long,
    @SerializedName("from_device") val fromDevice: Long,
    @SerializedName("to_device") val toDevice: Long,

    @SerializedName("send_date") val sendDate: Instant,

    @SerializedName("deliver_date") val deliverDate: Instant,
    val content: String,
    @TransactionStatus val status: Int,

    @SerializedName("last_update") val lastUpdate: Instant,
    val meta: String,
    @TransactionContentType @SerializedName("content_type") val contentType: Int,
    @TransactionType val type: Int,
)