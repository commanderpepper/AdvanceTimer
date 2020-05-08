package commanderpepper.advancetimer.ui.dismisstimer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import commanderpepper.App
import commanderpepper.advancetimer.room.AlarmTimer

class AlarmTimerDismissViewModel(application: Application) : AndroidViewModel(application) {
    private val alarmTimerViewModel = (application as App).appComponent.alarmTimerViewModelGenerator()

    suspend fun retrieveTimer(timerId: Int): AlarmTimer {
        return alarmTimerViewModel.getAlarmTimer(timerId)
    }

    suspend fun retrieveParentTimerTitle(timerId: Int): String {
        val timer = alarmTimerViewModel.getAlarmTimer(timerId)
        return if (timer.parentID != null) {
            alarmTimerViewModel.getAlarmTimer(timer.parentID).title
        } else {
            "This timer has no parent timer."
        }
    }

    suspend fun retrieveTimerTitle(timerId: Int): String {
        val timer = alarmTimerViewModel.getAlarmTimer(timerId)
        return timer.title
    }

    suspend fun retrieveChildTimers(timerId: Int): List<AlarmTimer> {
        return alarmTimerViewModel.getChildAlarmTimers(timerId)
    }

    suspend fun stopTimer(alarmTimerId: Int) {
        alarmTimerViewModel.disableAlarmTime(alarmTimerId)
    }
}