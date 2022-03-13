package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>
    suspend fun addOrUpdateUser(user: User)
}