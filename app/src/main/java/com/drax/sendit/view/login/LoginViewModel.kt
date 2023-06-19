package com.drax.sendit.view.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.drax.sendit.domain.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject
import kotlinx.coroutines.Job

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private var validatorJob: Job? = null

    fun loginWithEmail() {

    }

    fun validateInput(usernameInput: String, pattern: Pattern): Boolean {
        return usernameInput.matches(pattern.toRegex())
    }
}
