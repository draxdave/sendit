package com.drax.sendit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.data.db.model.Transaction

/**
 * Main database description.
 */
@Database(
    entities = [
        Registry::class,
        Transaction::class,
        Connection::class
    ],
    version = 19,
    exportSchema = false
)
@TypeConverters(TimeConverters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun registryDao() : RegistryDao
    abstract fun transactionDao() : TransactionsDao
    abstract fun connectionDao() : ConnectionDao
    abstract fun authDao() : AuthDao

    companion object {
        private var instance: AppDB? = null

        @Synchronized
        fun get(context: Context): AppDB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java, "${BuildConfig.APPLICATION_ID}_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            fillInDb(context.applicationContext,db as AppDB)
                        }
                    }).build()
            }
            return instance!!
        }

    }
}
