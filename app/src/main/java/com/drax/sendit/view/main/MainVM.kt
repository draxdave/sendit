package com.drax.sendit.view.main

import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.*

class MainVM(
    userRepository: UserRepository,
    deviceRepository: DeviceRepository
) : ResViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Neutral)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        job {
            userRepository.getUser().collect{ user ->
                _uiState.update {
                    when {
                        user == null ||
                                deviceRepository.getSelfDevice() == null
                        -> MainUiState.UserSignedOut
                        else -> MainUiState.UserSignedIn
                    }
                }
            }
        }
    }
}