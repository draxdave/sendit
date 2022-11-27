package com.drax.sendit

import android.app.Application
import com.drax.sendit.data.service.Analytics
import com.drax.sendit.data.service.Event
import dagger.hilt.android.HiltAndroidApp
import ir.drax.modal.Modal
import javax.inject.Inject


@HiltAndroidApp
class App: Application() {

    @Inject lateinit var analytics: Analytics

    override fun onCreate(){
        super.onCreate()


        Modal.init {
            blurEnabled = true
        }
        analytics.set(Event.App.Open)
    }
}