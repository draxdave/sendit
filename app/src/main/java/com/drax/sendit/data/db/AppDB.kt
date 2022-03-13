package com.drax.sendit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.drax.sendit.BuildConfig
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.data.db.model.Registry

/**
 * Main database description.
 */
@Database(
    entities = [
        Device::class,
        Registry::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDB : RoomDatabase() {
    abstract fun devicesDao() : DevicesDao
    abstract fun registryDao() : RegistryDao

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
                            fillInDb(context.applicationContext)
                        }
                    }).build()
            }
            return instance!!
        }
        fun getIfExists(): AppDB?{
            if(instance !=  null)
                return  instance
            return null
        }
        /**
         * fill database with tests data
         */
        private fun fillInDb(context: Context) { // Test data will be added here
        }
    }
}
