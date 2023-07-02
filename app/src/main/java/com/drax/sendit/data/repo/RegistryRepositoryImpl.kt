package com.drax.sendit.data.repo

import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.RegistryRepository
import com.drax.sendit.view.util.DeviceInfoHelper
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * RegistryRepository: Save and retrieve single value DB entries. for instance : User, Locale, Settings ..
 * Just define a constant number as record key and then save the value as a String value.
 * Use Gson to make sure the data stays the same while saving and loading.
 */
@Singleton
class RegistryRepositoryImpl @Inject constructor(
    private val registryDao: RegistryDao,
    private val deviceInfoHelper: DeviceInfoHelper,
    private val json: Json,
) : RegistryRepository {


    override suspend fun setFirebaseId(id: String) =
        registryDao.addOrUpdate(Registry(key = FIREBASE_ID, value = id))

    override fun getFirebaseId(): String? = registryDao.getRegistryValueSync(FIREBASE_ID)

    override suspend fun updateToken(token: String){
        registryDao.addOrUpdate(Registry(key = API_TOKEN, value = token))
    }

    override fun getApiToken(): String? = registryDao.getRegistryValueSync(API_TOKEN)

    override suspend fun updateThisDevice(device: DeviceDomain?) {
        store(THIS_DEVICE, device)
    }
    override fun getThisDevice() =
        registryDao.getRegistryValue(THIS_DEVICE).decode<DeviceDomain>(json)

    override suspend fun updateUser(user: User?) {
        store(THIS_USER, user)
    }

    override fun getUser() = registryDao.getRegistryValue(THIS_USER).decode<User>(json)

    override suspend fun updateQrUrl(qrUrl: String?) = store(QR_URL, qrUrl)


    override fun getQrUrl() = registryDao.getRegistryValue(QR_URL).decode<String>(json)

    override fun getDeviceId(): String =
        registryDao.getRegistryValueSync(DEVICE_UNIQUE_ID) ?: deviceInfoHelper.getId().also {
            registryDao.addOrUpdate(Registry(key = DEVICE_UNIQUE_ID, value = it))
        }

    private inline fun <reified T> store(key: String, value: T?) {
        registryDao.addOrUpdate(
            Registry(key = key, value = value.encode<T>(json))
        )
    }

    override suspend fun clearDb() {
        // Remove user related data
        updateToken("")
        updateThisDevice(null)
        updateUser(null)
    }

    companion object {
        private const val FIREBASE_ID = "FIREBASE_ID"
        private const val API_TOKEN = "API_TOKEN"
        private const val THIS_DEVICE = "THIS_DEVICE"
        private const val THIS_USER = "THIS_USER"
        private const val QR_URL = "QR_URL"
        private const val DEVICE_UNIQUE_ID = "DEVICE_UNIQUE_ID"
    }
}

private inline fun <reified T> Flow<String?>.decode(json: Json): Flow<T?> {

    return map {
        json.decodeFromString<T>(it ?: return@map null)
    }
}

private inline fun <reified T> T?.encode(json: Json): String? {
    return if (this == null) null
    else
        json.encodeToString(this)
}