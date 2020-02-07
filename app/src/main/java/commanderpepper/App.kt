package commanderpepper

import android.app.Application
import commanderpepper.advancetimer.BuildConfig
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RequestCodeGenerator.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}