package com.drax.sendit.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.drax.sendit.data.db.model.Registry
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrUpdate(registry: Registry)

    @Query("SELECT value FROM registry  WHERE `key`=:key  LIMIT 1 " )
    fun getRegistryValue(key:String):Flow<String?>

    @Query("SELECT value FROM registry  WHERE `key`=:key  LIMIT 1 " )
    fun getRegistryValueSync(key:String):String?

    @Update
    fun updateRegistry(registry: Registry)

    @Query("DELETE FROM registry")
    fun deleteAll()
}