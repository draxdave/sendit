package com.drax.sendit.view.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


inline fun <T> Flow<T>.observe(
    viewLifecycleOwner: LifecycleOwner,
    lifecycle: Lifecycle.State = Lifecycle.State.CREATED,
    crossinline action: suspend (value: T) -> Unit
) = viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(lifecycle) {
        collect {
            action.invoke(it)
        }
    }
}
