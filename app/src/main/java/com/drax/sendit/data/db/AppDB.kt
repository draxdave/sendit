package com.drax.sendit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.db.model.Registry
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.data.model.User
import com.drax.sendit.view.util.DeviceInfoHelper

/**
 * Main database description.
 */
@Database(
    entities = [
        Device::class,
        Registry::class,
        Transaction::class,
        User::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(TimeConverters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun devicesDao() : DevicesDao
    abstract fun registryDao() : RegistryDao
    abstract fun userDao() : UserDao
    abstract fun transactionDao() : TransactionsDao

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
        private fun getIfExists(): AppDB?{
            if(instance !=  null)
                return  instance
            return null
        }
        /**
         * fill database with tests data
         */
        @Suppress("UNUSED_PARAMETER")
        private fun fillInDb(context: Context, db: AppDB) { // Test data will be added here
        }
    }
}
