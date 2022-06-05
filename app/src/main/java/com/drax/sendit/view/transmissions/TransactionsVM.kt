package com.drax.sendit.view.transmissions

import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.view.TransactionWrapper
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update

class TransactionsVM(
    private val transactionRepository: TransactionRepository,
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository,
): ResViewModel() {


    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState.Neutral)
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        job {
            val thisDevice = deviceRepository.getSelfDevice().filterNotNull().single()
            val connections = connectionRepository.getConnections(onlyActive = false).single()

            transactionRepository.getAllTransactions().collect { transactionsList ->
                _uiState.update {
                    if (transactionsList.isEmpty())
                        TransactionsUiState.NoTransaction
                    else
                        TransactionsUiState.TransactionsLoaded(transactionsList.map {transaction ->
                            TransactionWrapper(
                                transaction = transaction,
                                isSender = thisDevice.id == transaction.broadcasterId,
                                thisDevice = thisDevice,
                                connection = connections.firstOrNull{ it.id == transaction.connectionId }
                            )
                        })
                }
            }
        }
    }

    fun removeTransaction(transactionWrapper: TransactionWrapper) = job {
        transactionRepository.removeLocally(transactionWrapper.transaction)
    }
}