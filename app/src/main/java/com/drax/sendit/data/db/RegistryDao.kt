package com.drax.sendit.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.drax.sendit.data.db.model.Registry

@Dao
interface RegistryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(registry: Registry)

    @Query("SELECT value FROM registry  WHERE `key`=:key  LIMIT 1 " )
    fun getRegistryValue(key:Int):LiveData<String?>

    @Query("SELECT value FROM registry  WHERE `key`=:key  LIMIT 1 " )
    fun getRegistryValueSync(key:Int):String?

    @Update
    fun updateRegistry(registry: Registry)

    @Query("DELETE FROM registry")
    fun deleteAll()
}