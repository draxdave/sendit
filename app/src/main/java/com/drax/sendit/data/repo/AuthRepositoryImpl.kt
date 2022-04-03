package com.drax.sendit.data.repo

import com.drax.sendit.domain.network.ApiService
import com.drax.sendit.domain.network.NetworkCall
import com.drax.sendit.domain.network.model.SignInRequest
import com.drax.sendit.domain.repo.AuthRepository
import com.drax.sendit.domain.repo.DeviceRepository
import com.drax.sendit.domain.repo.TransactionRepository
import com.drax.sendit.domain.repo.UserRepository
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val deviceRepository: DeviceRepository,
): AuthRepository {

    override fun signInDevice(signInRequest: SignInRequest) = flow {
        emit(
            NetworkCall {
                apiService.signIn(signInRequest)
            }.fetch()
        )
    }

    override fun signOutDevice() = flow {

        NetworkCall {
            apiService.signOut()
        }.fetch()
            .also {
                emit(it)
                userRepository.clearDb()
                transactionRepository.clearDb()
                deviceRepository.clearDb()
            }
    }
}