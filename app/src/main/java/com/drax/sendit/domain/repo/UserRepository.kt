package com.drax.sendit.domain.repo

import com.drax.sendit.data.model.User
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
interface UserRepository : BaseStorageRepository {
    //    fun getUserSync(): User?
    fun getUser(): Flow<User?>
    suspend fun addOrUpdateUser(user: User)
}