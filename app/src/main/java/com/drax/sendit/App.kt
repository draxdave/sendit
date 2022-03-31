package com.drax.sendit

import android.app.Application
import com.drax.sendit.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate(){
        super.onCreate()
        // start Koin!
        startKoin {
//            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)


            // declare used Android context
            androidContext(this@App)

            // declare modules
            modules(appModule)
        }
        

    }
}