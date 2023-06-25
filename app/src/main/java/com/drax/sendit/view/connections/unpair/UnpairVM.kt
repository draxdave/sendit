package com.drax.sendit.view.connections.unpair

import androidx.compose.runtime.mutableStateOf
import com.drax.sendit.data.model.Resource
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.domain.repo.ConnectionRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class UnpairVM @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val analytics: Analytics,
) : ResViewModel() {

    internal val uiState = mutableStateOf<UnpairUiState>(UnpairUiState.Neutral)

    fun unpairDevice(unpairRequest: UnpairRequest) {
        uiState.value = UnpairUiState.Loading
        job {
//            connectionRepository.unpair(unpairRequest).collect { unpair ->
//                uiState.value = when (unpair) {
//                    is Resource.ERROR -> UnpairUiState.Failed(unpair)
//                    is Resource.SUCCESS -> {
//                        analytics.set(Event.Connections.Unpaired)
//                        UnpairUiState.Done
//                    }
//                }
//            }
            delay(1000)
            uiState.value = UnpairUiState.Done
        }
    }
}
