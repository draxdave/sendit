package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.User
import kotlinx.coroutines.flow.Flow

interface RegistryRepository {
    suspend fun setFirebaseId(id:String)
    fun getFirebaseId():String?

    fun getUser(): Flow<User?>
    suspend fun setUser(user: User)
}