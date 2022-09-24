package com.drax.sendit.view.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


public inline fun <T> Fragment.collect(flow: Flow<T>, crossinline action: suspend (value: T) -> Unit): Unit {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
            flow.collect {
                action.invoke(it)
            }
        }
    }
}