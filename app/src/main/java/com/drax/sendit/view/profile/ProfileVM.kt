package com.drax.sendit.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.main.MainUiState
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

class ProfileVM(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ResViewModel() {

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Neutral)
    val uiState: StateFlow<ProfileUiState> = _uiState

    val user: LiveData<User?> = userRepository.getUser()
        .asLiveData()

    fun signOut(){
        job {
            authRepository.signOutDevice().collect()
        }
    }
}