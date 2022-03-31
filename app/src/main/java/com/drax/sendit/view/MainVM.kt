package com.drax.sendit.view

import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.UserRepository
import com.drax.sendit.view.util.ResViewModel
import kotlinx.coroutines.flow.Flow

class MainVM(
    userRepository: UserRepository
) : ResViewModel() {

    val user: Flow<User?> = userRepository.getUser()
}