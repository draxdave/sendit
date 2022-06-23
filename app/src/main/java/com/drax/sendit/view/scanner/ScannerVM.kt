package com.drax.sendit.view.scanner

import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScannerVM: ResViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Neutral)
    val uiState: StateFlow<ScannerUiState> = _uiState

}