package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import commanderpepper.App
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import timber.log.Timber

class AlarmTimerNewViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmTimerViewModel: AlarmTimerViewModel = (application as App).appComponent.alarmTimerViewModelGenerator()

    fun saveAlarm(context: Context, triggerAtMillis: Long) {
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }

    fun saveTimer(context: Context, triggerAtMillis: Long) {
        Timber.d("Inside Alarm Timer New ViewModel")
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }

    fun makeTimerUsingContext(title: String, context: Context, triggerAtMillis: Long) {
//        alarmTimerViewModel.makeTimerUsingContext(context, triggerAtMillis)
//        alarmTimerViewModel.createTimer(title, context, triggerAtMillis)
        alarmTimerViewModel.createTimerWaitForId(title, context, triggerAtMillis)
    }
}