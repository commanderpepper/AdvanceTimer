package commanderpepper

import android.app.Application
import android.content.Context
import commanderpepper.advancetimer.BuildConfig
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.ui.alarmtimerlist.AlarmListViewModel
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppContextModule::class,
        RequestCodeGeneratorModule::class,
        AlarmTimerViewModelModule::class]
)
interface ApplicationComponent {

    fun requestCodeGenerator(): RequestCodeGenerator

    fun alarmTimerViewModelGenerator(): AlarmTimerViewModel

    /**
     * Inject into the UI View Model
     */
    fun injectAlarmListViewModel(alarmListViewModel: AlarmListViewModel)

    /**
     * Inject into alarm creator
     */
    fun injectAlarmCreator(alarmCreator: AlarmCreator)

    /**
     * Application context
     */
    fun getContext(): Context
}

@Module
internal class AppContextModule(
    private val mContext: Context
) {
    @Singleton
    @Provides
    fun getContext() = mContext
}

@Module(includes = [AppContextModule::class])
internal class RequestCodeGeneratorModule {
    @Singleton
    @Provides
    fun getRequestCodeGenerator(context: Context) = RequestCodeGenerator(context)
}

@Module(includes = [AppContextModule::class])
internal class AlarmTimerViewModelModule {
    @Singleton
    @Provides
    fun getAlarmTimerViewModel(context: Context) = AlarmTimerViewModel(
        AlarmRepository(context),
        AlarmCreator(context)
    )
}

class App : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /**
         * To be used in the entire app
         */
        appComponent = DaggerApplicationComponent.builder()
            .appContextModule(AppContextModule(this.applicationContext))
            .requestCodeGeneratorModule(RequestCodeGeneratorModule())
            .alarmTimerViewModelModule(AlarmTimerViewModelModule())
            .build()
    }
}


