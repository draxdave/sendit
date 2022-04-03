package com.drax.sendit.data.db

import androidx.room.*
import com.drax.sendit.data.db.model.Connection
import kotlinx.coroutines.flow.Flow

@Dao
interface  ConnectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg connection: Connection)

    @Query("SELECT * FROM connection" )
    fun getList(): Flow<List<Connection>>

    @Delete
    fun delete(connection: Connection)

    @Query("DELETE FROM connection WHERE id = :id")
    fun deleteById(id: Long)

    @Update fun update(connection: Connection)

    @Query("DELETE FROM connection")
    fun deleteAll()

}