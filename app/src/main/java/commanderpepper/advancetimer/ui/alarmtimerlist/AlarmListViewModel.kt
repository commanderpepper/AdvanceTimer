package commanderpepper.advancetimer.ui.alarmtimerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.withContext
import timber.log.Timber

class AlarmListViewModel : ViewModel() {
    private val alarmTimerViewModel = AlarmTimerViewModel.get()

    suspend fun getParentAlarmTimerList(): List<AlarmTimer> {
        Timber.d("Getting parent timers")
        return withContext(viewModelScope.coroutineContext) {
            alarmTimerViewModel.getParentAlarmTimers()
        }
    }

}