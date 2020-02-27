package commanderpepper

import android.app.Application
import android.content.Context
import commanderpepper.advancetimer.BuildConfig
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [AppContextModule::class]
)
interface ApplicationComponent {
    //    fun injectAlarmCreator(alarmCreator: AlarmCreator)
//
    fun requestCodeGenerator(): RequestCodeGenerator

    /**
     * Inject into alarm creator
     */
    fun injectAlarmCreator(alarmCreator: AlarmCreator)

    @Named("ApplicationContext")
    fun getApplicationContext(): Context
}

@Module
internal class AppContextModule(
    private val mContext: Context
) {
    @Provides
    @Named("ApplicationContext")
    fun getContext() = mContext
}

@Module
internal class RequestCodeGeneratorModule(
    private val mRcg: RequestCodeGenerator
) {
    @Provides
    fun getRequestCodeGenerator() = mRcg
}


class App : Application() {

    /**
     * To be used in the entire app
     */
    val appComponent: ApplicationComponent = DaggerApplicationComponent.builder()
        .appContextModule(AppContextModule(this.applicationContext))
        .build()

    private lateinit var requestCodeGenerator: RequestCodeGenerator

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}


