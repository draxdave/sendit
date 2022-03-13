package com.drax.sendit.view.login

import androidx.lifecycle.viewModelScope
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoginVM(
    private val userRepository: UserRepository
): ResViewModel() {
    val versionText="Version A.${BuildConfig.VERSION_NAME}"


    fun login(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.addOrUpdateUser(user)
    }
}