package com.drax.sendit.view.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ResViewModel:ViewModel() {
    val isLoading = MutableLiveData(false)
    val isEmpty = MutableLiveData(true)
}