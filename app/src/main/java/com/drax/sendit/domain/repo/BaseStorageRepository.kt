package com.drax.sendit.domain.repo

interface BaseStorageRepository {
    suspend fun clearDb()
}