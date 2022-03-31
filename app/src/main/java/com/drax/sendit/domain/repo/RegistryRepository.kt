package com.drax.sendit.domain.repo

interface RegistryRepository {
    suspend fun setFirebaseId(id:String)
    fun getFirebaseId():String?

    suspend fun clearData()
}