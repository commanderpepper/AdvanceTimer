package commanderpepper

import android.app.Application
import commanderpepper.advancetimer.BuildConfig
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.repository.AlarmRepository
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RequestCodeGenerator.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AlarmRepository.initialize(this)
        RequestCodeGenerator.initialize(this)
        AlarmCreator.initialize(this)
//        val alarmRepository = AlarmRepository.get()
//        val alarmCreator = AlarmCreator.get()
    }
}