package com.drax.sendit.data.db

import androidx.room.*
import com.drax.sendit.data.db.model.Device
import kotlinx.coroutines.flow.Flow

@Dao
interface  DevicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(device: Device) : Long

    @Query("SELECT * FROM device" )
    fun getList(): Flow<List<Device>>

    @Query("SELECT * FROM device" )
    fun getAll():List<Device>

    @Delete
    fun delete(device: Device)

    @Query("DELETE FROM device WHERE iid = :iid")
    fun deleteById(iid: Long)

    @Update fun update(device: Device)

    @Query("DELETE FROM device")
    fun deleteAll()

}