package commanderpepper.advancetimer.ui.alarmtimernew

import android.content.Context
import androidx.lifecycle.ViewModel
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel

class AlarmTimerNewViewModel : ViewModel() {
    private val alarmTimerViewModel = AlarmTimerViewModel.get()

    fun saveAlarm(context: Context, triggerAtMillis: Long) {
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }

    fun saveTimer(context: Context, triggerAtMillis: Long) {
        alarmTimerViewModel.addOneOffParentTimer(context, triggerAtMillis)
    }
}