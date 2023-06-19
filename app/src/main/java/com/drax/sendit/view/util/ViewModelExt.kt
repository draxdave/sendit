package com.drax.sendit.view.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.job(
    dispatcher: CoroutineContext = Dispatchers.IO,
    job: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(dispatcher, CoroutineStart.DEFAULT, job)