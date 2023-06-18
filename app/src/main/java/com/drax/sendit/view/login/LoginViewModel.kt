package com.drax.sendit.view.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


class LoginViewModel: ViewModel() {
    private val _formState = mutableStateOf<FormState>(FormState.Login)
    val formState: State<FormState> = _formState

    fun updateFormState(newState: FormState) {
        _formState.value = newState
    }

}
