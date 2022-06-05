package com.drax.sendit.domain.network.model.type

import androidx.annotation.IntDef
import com.drax.sendit.domain.network.model.type.PairResponseType.Companion.PairResponseType_ACCEPT
import com.drax.sendit.domain.network.model.type.PairResponseType.Companion.PairResponseType_DECLINE

@IntDef(PairResponseType_ACCEPT, PairResponseType_DECLINE)
@Retention(AnnotationRetention.SOURCE)
annotation class PairResponseType {

    companion object{
        const val PairResponseType_ACCEPT = 200
        const val PairResponseType_DECLINE = 100
    }
}