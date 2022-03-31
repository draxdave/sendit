package com.drax.sendit.data.db

import androidx.room.*
import com.drax.sendit.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: User)

    @Update fun update(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<User?>

    @Query("DELETE FROM user")
    fun deleteAll()
}