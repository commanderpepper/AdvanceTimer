package commanderpepper.advancetimer.ui.alarmtimernew

import android.content.Context
import androidx.lifecycle.ViewModel
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import timber.log.Timber

class AlarmTimerNewViewModel : ViewModel() {
    private val alarmTimerViewModel = AlarmTimerViewModel.get()

    fun saveAlarm(context: Context, triggerAtMillis: Long) {
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }

    fun saveTimer(context: Context, triggerAtMillis: Long) {
        Timber.d("Inside Alarm Timer New ViewModel")
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }

    fun makeTimerUsingContext(context: Context, triggerAtMillis: Long) {
        alarmTimerViewModel.makeTimerUsingContext(context, triggerAtMillis)
    }
}