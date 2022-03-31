package com.drax.sendit.data.model

import com.drax.sendit.R


sealed class ModalMessage(
    val title: String,
    val icon: Int,
    val fromTop: Boolean,
    val lock: Boolean,
) {
    data class Success(val message: String): ModalMessage(
        title = message,
        icon = R.drawable.tick,
        fromTop = true,
        lock = false
    )


    data class Failed(val message: String): ModalMessage(
        title = message,
        icon = R.drawable.warning,
        fromTop = true,
        lock = false
    )

    data class Neutral(val message: String): ModalMessage(
        title = message,
        icon = R.drawable.ic_outline_info_24,
        fromTop = true,
        lock = false
    )
}