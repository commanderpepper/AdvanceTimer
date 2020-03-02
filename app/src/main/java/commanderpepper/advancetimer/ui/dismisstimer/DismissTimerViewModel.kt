package commanderpepper.advancetimer.ui.dismisstimer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.App
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.withContext

class DismissTimerViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmTimerViewModel: AlarmTimerViewModel =
        (application as App).appComponent.alarmTimerViewModelGenerator()

    suspend fun retrieveTimer(timerId: Int): AlarmTimer {
//        return withContext(viewModelScope.coroutineContext) {
//            alarmTimerViewModel.getAlarmTimer(timerId)
//        }
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

    suspend fun retrieveChildTimers(timerId: Int): List<AlarmTimer> {
        return alarmTimerViewModel.getChildAlarmTimers(timerId)
    }
}