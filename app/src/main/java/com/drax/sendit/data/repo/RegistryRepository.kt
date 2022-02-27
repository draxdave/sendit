package com.drax.sendit.data.repo

import com.drax.sendit.data.db.RegistryDao
import com.drax.sendit.data.db.model.Registry
import com.google.gson.Gson


/**
 * RegistryRepository: Save and retrieve single value DB entries. for instance : User, Locale, Settings ..
 * Just define a constant number as record key and then save the value as a String value.
 * Use Gson to make sure the data stays the same while saving and loading.
 */
class  RegistryRepository (
    private val registryDao: RegistryDao
){
    private val g = Gson()
    companion object{
        private final const val FIREBASE_ID = 1
    }

    suspend fun setFirebaseId(id:String) = registryDao.add(Registry(key = FIREBASE_ID,value = id))
    suspend fun getFirebaseId():String? = registryDao.getRegistryValueSync(FIREBASE_ID)

}