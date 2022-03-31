package com.drax.sendit.view.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ResViewModel: ViewModel() {
    protected fun setLoading(isLoading:Boolean){
        if (isLoading) activeLoading++
        else activeLoading--
    }

    private var activeLoading:Int = 0
        set(value) {
            field = value
            _isLoading.postValue(field != 0)
        }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
}