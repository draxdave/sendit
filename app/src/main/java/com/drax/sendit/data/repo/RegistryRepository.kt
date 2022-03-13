package com.drax.sendit.data.repo

import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.data.model.User
import com.drax.sendit.domain.repo.RegistryRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject


/**
 * RegistryRepository: Save and retrieve single value DB entries. for instance : User, Locale, Settings ..
 * Just define a constant number as record key and then save the value as a String value.
 * Use Gson to make sure the data stays the same while saving and loading.
 */
class  RegistryRepositoryImpl(
    private val registryDao: RegistryDao,
): RegistryRepository {
    companion object{
        private const val FIREBASE_ID = "FIREBASE_ID"
        private const val USER = "USER"
    }

    override suspend fun setFirebaseId(id:String) = registryDao.add(Registry(key = FIREBASE_ID,value = id))
    override fun getFirebaseId():String? = registryDao.getRegistryValueSync(FIREBASE_ID)

    override suspend fun setUser(user: User) = registryDao.add(Registry(key = USER,value = Json.encodeToString(User.serializer(), user)))
    override fun getUser(): Flow<User?> {
        return registryDao.getRegistryValue(USER).map {
            if (it == null)
                null
            else
                Json.decodeFromString(User.serializer(), it)
        }
    }

}