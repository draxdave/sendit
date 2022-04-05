package com.drax.sendit.view.scanner

import com.drax.sendit.BuildConfig
import com.drax.sendit.R
import com.drax.sendit.data.model.Resource
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.PushRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

class ScannerVM(
    ): ResViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.Neutral)
    val uiState: StateFlow<ScannerUiState> = _uiState

    init {

    }

}