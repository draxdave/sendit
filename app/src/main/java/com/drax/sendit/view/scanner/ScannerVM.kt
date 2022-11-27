package com.drax.sendit.view.scanner

import com.drax.sendit.view.util.ResViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ScannerVM @Inject constructor(): ResViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Neutral)
    val uiState: StateFlow<ScannerUiState> = _uiState

}