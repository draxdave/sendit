package com.drax.sendit.data.repo

import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.RegistryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder


/**
 * RegistryRepository: Save and retrieve single value DB entries. for instance : User, Locale, Settings ..
 * Just define a constant number as record key and then save the value as a String value.
 * Use Gson to make sure the data stays the same while saving and loading.
 */
class  RegistryRepositoryImpl(
    private val registryDao: RegistryDao,
): RegistryRepository {


    override suspend fun setFirebaseId(id:String) = registryDao.addOrUpdate(Registry(key = FIREBASE_ID,value = id))
    override fun getFirebaseId():String? = registryDao.getRegistryValueSync(FIREBASE_ID)

    override suspend fun updateToken(token: String) = registryDao.addOrUpdate(Registry(key = API_TOKEN,value = token))
    override fun getApiToken(): String? = registryDao.getRegistryValueSync(API_TOKEN)

    override suspend fun updateThisDevice(device: Device?) = store(THIS_DEVICE,device)
    override fun getThisDevice() =  registryDao.getRegistryValue(THIS_DEVICE).decode<Device>()

    override suspend fun updateUser(user: User?){
        store(THIS_USER, user)
    }
    override fun getUser() =  registryDao.getRegistryValue(THIS_USER).decode<User>()

    override suspend fun updateQrUrl(qrUrl: String?) = store(QR_URL, qrUrl)


    override fun getQrUrl() =  registryDao.getRegistryValue(QR_URL).decode<String>()

    private inline fun <reified T> store(key: String, value: T?){
        registryDao.addOrUpdate(
            Registry(key = key, value = value.encode<T>())
        )
    }

    override suspend fun clearDb() {
        // Remove user related data
        updateToken("")
        updateThisDevice(null)
        updateUser(null)
    }

    companion object{
        private const val FIREBASE_ID = "FIREBASE_ID"
        private const val API_TOKEN = "API_TOKEN"
        private const val THIS_DEVICE = "THIS_DEVICE"
        private const val THIS_USER = "THIS_USER"
        private const val QR_URL = "QR_URL"
    }
}

private inline fun <reified T> Flow<String?>.decode(): Flow<T?> {

    return map {
        if(it == null) null
        else
            Json.decodeFromString<T>(it)
    }
}

private inline fun <reified T> String?.decode(): T? {
    return if(this == null) null
    else
        Json.decodeFromString<T>(this)
}

private inline fun <reified T> T?.encode(): String? {
    return if(this == null) null
    else
        Json.encodeToString(this)
}