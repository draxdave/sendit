package com.drax.sendit.domain.repo

interface LocalStorageRepository {
    suspend fun clearDb()
}