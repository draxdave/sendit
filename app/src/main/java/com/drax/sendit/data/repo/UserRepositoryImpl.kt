package com.drax.sendit.data.repo

import com.drax.sendit.data.model.User
import com.drax.sendit.data.model.UserType
import com.drax.sendit.domain.repo.DevicesRepository
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val registryRepository: RegistryRepository,
    private val devicesRepository: DevicesRepository
) : UserRepository {
    override fun getUser(): Flow<User> = registryRepository.getUser().map {
        it ?:
        User.createGuestUser()
    }

    override suspend fun addOrUpdateUser(user: User) = registryRepository.setUser(user)
    override suspend fun signOutAndClearUserData() {
        registryRepository.clearData()
        devicesRepository.clearData()
    }
}