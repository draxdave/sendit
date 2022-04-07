package com.drax.sendit.data.model

import com.drax.sendit.domain.network.InstantDeserializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant


object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {

        return InstantDeserializer().deserialize(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Instant) {

        encoder.encodeString(InstantDeserializer().serialize(value))
    }
}

//fun String.toInstant(): Instant{
////    if (contains("z", true))
//    replace("z"," ",false)
//    replace("t"," ",false)
//    return LocalDateTime.parse(
//        this,
//        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//    )
//        .atZone(ZoneId.systemDefault())
//        .toInstant()
//}