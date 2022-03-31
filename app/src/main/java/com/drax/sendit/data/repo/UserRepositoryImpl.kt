package com.drax.sendit.data.repo

import com.drax.sendit.data.db.UserDao
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {
    override fun getUser(): Flow<User?> = userDao.getUser()

    override suspend fun addOrUpdateUser(user: User) = userDao.update(user)
    override suspend fun clearDb() {
        userDao.deleteAll()
    }
}