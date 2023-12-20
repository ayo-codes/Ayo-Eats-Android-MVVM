package ie.setu.ayoeats.main

import android.app.Application
import timber.log.Timber

class AyoEatsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("AyoEats Application has started")

    }
}