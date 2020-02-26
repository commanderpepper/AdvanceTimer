package commanderpepper

import android.app.Application
import commanderpepper.advancetimer.BuildConfig
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import dagger.Component
import timber.log.Timber
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {
    fun injectAlarmCreator(alarmCreator: AlarmCreator)

    fun requestCodeGenerator(): RequestCodeGenerator
}

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RequestCodeGenerator.initialize(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val appComponent = DaggerApplicationComponent.create()

        AlarmRepository.initialize(this)
        RequestCodeGenerator.initialize(this)
        AlarmCreator.initialize(this)
        AlarmTimerViewModel.initialize(AlarmRepository.get(), AlarmCreator.get())
//        val alarmRepository = AlarmRepository.get()
//        val alarmCreator = AlarmCreator.get()
    }
}