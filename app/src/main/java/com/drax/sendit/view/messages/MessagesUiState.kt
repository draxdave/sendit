package com.drax.sendit.view.messages

import com.drax.sendit.view.MessageWrapper

sealed class MessagesUiState {
    object Neutral : MessagesUiState()
    object NoTransaction : MessagesUiState()
    data class MessagesLoaded(val transmissions: List<MessageWrapper>) : MessagesUiState()
}