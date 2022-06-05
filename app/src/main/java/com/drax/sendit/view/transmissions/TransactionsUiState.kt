package com.drax.sendit.view.transmissions

import com.drax.sendit.view.TransactionWrapper

sealed class TransactionsUiState {
    object Neutral : TransactionsUiState()
    object NoTransaction : TransactionsUiState()
    data class TransactionsLoaded(val transmissions: List<TransactionWrapper>) : TransactionsUiState()
}