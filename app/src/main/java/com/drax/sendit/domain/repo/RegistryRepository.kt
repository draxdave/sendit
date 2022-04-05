package com.drax.sendit.domain.repo

import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.model.User
import kotlinx.coroutines.flow.Flow

interface RegistryRepository: BaseStorageRepository {
    suspend fun setFirebaseId(id:String)
    fun getFirebaseId():String?

    suspend fun updateToken(token:String)
    fun getApiToken():String?

    suspend fun updateThisDevice(device: Device?)
    fun getThisDevice():Flow<Device?>

    suspend fun updateUser(user: User?)
    fun getUser(): Flow<User?>

    suspend fun updateQrUrl(qrUrl: String?)
    fun getQrUrl(): Flow<String?>
}