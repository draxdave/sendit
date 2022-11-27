package com.drax.sendit.view.main

import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class MainVM @Inject constructor(
    userRepository: UserRepository,
    deviceRepository: DeviceRepository,
) : ResViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Neutral)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        job {
            userRepository.getUser().collect { user ->
                _uiState.tryEmit(
                    when {
                        user == null || deviceRepository.getSelfDevice().firstOrNull() == null
                        -> MainUiState.UserSignedOut
                        else -> {
                            if (uiState.value == MainUiState.UserSignedOut) delay(5000)
                            MainUiState.UserSignedIn
                        }
                    }
                )
            }
        }
    }
}