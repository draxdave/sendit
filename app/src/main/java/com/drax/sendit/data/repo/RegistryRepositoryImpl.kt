package com.drax.sendit.data.repo

import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.domain.repo.RegistryRepository


/**
 * RegistryRepository: Save and retrieve single value DB entries. for instance : User, Locale, Settings ..
 * Just define a constant number as record key and then save the value as a String value.
 * Use Gson to make sure the data stays the same while saving and loading.
 */
class  RegistryRepositoryImpl(
    private val registryDao: RegistryDao,
): RegistryRepository {

    override suspend fun setFirebaseId(id:String) = registryDao.add(Registry(key = FIREBASE_ID,value = id))
    override fun getFirebaseId():String? = registryDao.getRegistryValueSync(FIREBASE_ID)

    override suspend fun clearData() {
        registryDao.deleteAll()
    }

    companion object{
        private const val FIREBASE_ID = "FIREBASE_ID"
    }
}