package com.drax.sendit.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.drax.sendit.data.db.model.Device

@Dao
interface  DevicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(device: Device) : Long

    @Query("SELECT * FROM devices" )
    fun getList(): LiveData<List<Device>>

    @Query("SELECT * FROM devices" )
    fun getAll():List<Device>

    @Delete
    fun delete(device: Device)

    @Query("DELETE FROM devices WHERE instanceId = :deviceId")
    fun delete(deviceId: String)

    @Update
    fun update(device: Device)

    @Query("DELETE FROM devices")
    fun deleteAll()

}