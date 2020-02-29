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
        return withContext(viewModelScope.coroutineContext) {
            alarmTimerViewModel.getAlarmTimer(timerId)
        }
    }
}