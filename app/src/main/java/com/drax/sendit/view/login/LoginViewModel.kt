package com.drax.sendit.view.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.drax.sendit.domain.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
): ViewModel() {

    private val _formType = mutableStateOf<FormType>(FormType.Login)
    val formType: State<FormType>
        get () = _formType

    private val _formState = mutableStateOf<FormState>(FormState.Loading)
    val formState: State<FormState>
        get () = _formState


    fun updateFormState(newState: FormType) {
        _formType.value = newState
    }

    fun loginWithEmail() {

    }
}
