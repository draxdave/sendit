package com.drax.sendit.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import com.drax.sendit.view.util.job

class ProfileVM(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ResViewModel() {

    val user: LiveData<User?> = userRepository.getUser()
        .asLiveData()

    fun signOut(){
        job {
            authRepository.signOutDevice()
        }
    }
}