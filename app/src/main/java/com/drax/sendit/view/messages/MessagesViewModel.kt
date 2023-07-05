package com.drax.sendit.view.messages

import androidx.compose.runtime.mutableStateOf
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import formatToDate
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository,
) : ResViewModel() {
    val uiState = mutableStateOf<UiState>(UiState.Loading)

    init {
        job {

            transactionRepository.getAllTransactions().map { messagesList ->
                    val thisDevice = deviceRepository.getSelfDevice().firstOrNull()
                    val connections =
                        connectionRepository.getConnections(onlyActive = false).firstOrNull()
                    messagesList.map { message ->
                        MessageWrapper(message = message,
                            isSender = thisDevice?.id == message.broadcasterId,
                            thisDevice = thisDevice,
                            connection = connections?.firstOrNull { it.id == message.connectionId })
                    }
                }.collect { transactionsList ->

                    withContext(Dispatchers.Main) {
                        if (transactionsList.isEmpty()) {
                            uiState.value = UiState.Empty
                        } else {
                            uiState.value = UiState.WithMessages(transactionsList.map {
                                MessageUiModel(
                                    id = it.message.id,
                                    isSender = it.isSender,
                                    message = it.message.content,
                                    partyName = it.connection?.name ?: "Unknown",
                                    thumbnail = it.connection?.iconUrl ?: "",
                                    addedDate = it.message.sendDate.formatToDate(MessageUiModel.dateFormat),
                                )
                            })
                        }
                    }
                }
        }
    }

    fun removeTransaction(messageId: Long) = job {
        transactionRepository.removeLocallyById(messageId)
    }
}

data class MessageUiModel(
    val id: Long,
    val isSender: Boolean,
    val message: String,
    val addedDate: String,
    val partyName: String,
    val thumbnail: String,
) {

    companion object {
        const val dateFormat = "MMMM dd HH:mm"
    }
}
