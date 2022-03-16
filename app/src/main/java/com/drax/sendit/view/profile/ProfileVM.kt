package com.drax.sendit.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.devices.job
import com.drax.sendit.view.util.ResViewModel

class ProfileVM(
    private val userRepository: UserRepository
): ResViewModel() {

    val user: LiveData<User> = userRepository.getUser()
        .asLiveData()

    fun signOut(){
        job {
            userRepository.signOutAndClearUserData()
        }
    }
}