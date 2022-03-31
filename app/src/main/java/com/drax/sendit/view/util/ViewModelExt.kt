package com.drax.sendit.view.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun ViewModel.job(dispatcher: CoroutineContext = Dispatchers.IO, job: suspend CoroutineScope.() -> Unit){
    viewModelScope.launch(dispatcher, CoroutineStart.DEFAULT, job)
}