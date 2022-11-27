package com.drax.sendit.view.messages

import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.view.MessageWrapper
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MessagesVM @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository,
): ResViewModel() {

    private val _uiState = MutableStateFlow<MessagesUiState>(MessagesUiState.Neutral)
    val uiState: StateFlow<MessagesUiState> = _uiState

    val messagesList = transactionRepository.getAllTransactions()
        .map { messagesList ->
            val thisDevice = deviceRepository.getSelfDevice().firstOrNull()
            val connections = connectionRepository.getConnections(onlyActive = false).firstOrNull()
            messagesList.map { message ->
                MessageWrapper(
                    message = message,
                    isSender = thisDevice?.id == message.broadcasterId,
                    thisDevice = thisDevice,
                    connection = connections?.firstOrNull { it.id == message.connectionId }
                )
            }
        }
        .onEach { transactionsList ->
            _uiState.tryEmit(
                if (transactionsList.isEmpty())
                    MessagesUiState.NoTransaction
                else
                    MessagesUiState.MessagesLoaded(transactionsList)
            )
        }

    fun removeTransaction(messageWrapper: MessageWrapper) = job {
        transactionRepository.removeLocally(messageWrapper.message)
    }
}
