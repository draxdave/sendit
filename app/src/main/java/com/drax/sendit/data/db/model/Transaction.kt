package com.drax.sendit.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drax.sendit.data.model.InstantSerializer
import com.drax.sendit.domain.network.model.type.TransactionContentType
import com.drax.sendit.domain.network.model.type.TransactionStatus
import com.drax.sendit.domain.network.model.type.TransactionType
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.time.Instant

@Entity
@Serializable
data class Transaction(
    @PrimaryKey(autoGenerate = true)  val iid: Long=0,

    val id: Long,
    @SerializedName("from_device")
    @SerialName("from_device")
    val fromDevice: Long,
    @SerializedName("to_device")
    @SerialName("to_device")
    val toDevice: Long,

    @SerializedName("send_date")
    @SerialName("send_date")
    @Serializable(with = InstantSerializer::class) val sendDate: Instant,

    @SerializedName("deliver_date")
    @SerialName("deliver_date")
    @Serializable(with = InstantSerializer::class) val deliverDate: Instant?,
    val content: String,
    @TransactionStatus val status: Int,

    @SerializedName("last_update")
    @SerialName("last_update")
    @Serializable(with = InstantSerializer::class)  val lastUpdate: Instant,
    val meta: String,

    @TransactionContentType
    @SerializedName("content_type")
    @SerialName("content_type")
    val contentType: Int,

    @TransactionType val type: Int,
): java.io.Serializable