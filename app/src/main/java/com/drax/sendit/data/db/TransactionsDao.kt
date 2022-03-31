package com.drax.sendit.data.db

import androidx.room.*
import com.drax.sendit.data.db.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface  TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(transaction: Transaction) : Long

    @Query("SELECT * FROM `transaction` ORDER BY sendDate" )
    fun getList(): Flow<List<Transaction>>

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM `transaction` WHERE iid = :iid")
    fun deleteById(iid: Long)

    @Update fun update(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    fun deleteAll()

}