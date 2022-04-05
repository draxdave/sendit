package com.drax.sendit.data.model

import com.drax.sendit.R
import com.drax.sendit.domain.network.AuthInterceptor.Companion.UNAUTHORIZED_ACCESS
import com.drax.sendit.domain.network.ErrorHandlerInterceptor
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.BadRequest
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.ConnectException
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.SocketTimeoutException
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.UnControlledBadRequest
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.UnControlledRedirection
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.UnControlledServerError
import com.drax.sendit.domain.network.ErrorHandlerInterceptor.Companion.UnknownHostException


sealed class ModalMessage(
    val title: Int,
    val icon: Int,
    val fromTop: Boolean,
    val lock: Boolean,
) {
    data class Success(val message: Int): ModalMessage(
        title = message,
        icon = R.drawable.tick,
        fromTop = true,
        lock = false
    )


    data class Failed(val message: Int): ModalMessage(
        title = message,
        icon = R.drawable.warning,
        fromTop = true,
        lock = false
    )

    data class FromNetError(val errorCode: Int): ModalMessage(
        title = errorCode.toStringId(),
        icon = errorCode.toDrawableId(),
        fromTop = true,
        lock = false
    )

    data class Neutral(val message: Int): ModalMessage(
        title = message,
        icon = R.drawable.ic_outline_info_24,
        fromTop = true,
        lock = false
    )
}

private fun Int.toStringId() = when(this){
    UNAUTHORIZED_ACCESS -> R.string.error_unauthorized
    ConnectException -> R.string.network_unavailable
    SocketTimeoutException-> R.string.network_unavailable
    UnknownHostException->R.string.network_unavailable
    ErrorHandlerInterceptor.Exception-> R.string.error_internal
    BadRequest -> R.string.error_internal
    in UnControlledBadRequest -> R.string.error_internal
    in UnControlledRedirection-> R.string.error_internal
    in UnControlledServerError -> R.string.error_internal
    else -> R.string.unknown_error
}

private fun Int.toDrawableId() = when(this){
    UNAUTHORIZED_ACCESS -> R.drawable.ic_round_signal_cellular_connected_no_internet_4_bar_24

    UnknownHostException,
    SocketTimeoutException ,
    ConnectException ->
        R.drawable.ic_round_signal_cellular_connected_no_internet_4_bar_24

    else -> R.drawable.warning
}