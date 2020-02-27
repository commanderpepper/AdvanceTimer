package commanderpepper.advancetimer.ui.alarmtimerlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.App
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AlarmListViewModel(application: Application): AndroidViewModel(application) {

   val alarmTimerViewModel: AlarmTimerViewModel = (application as App).appComponent.alarmTimerViewModelGenerator()

    suspend fun getParentAlarmTimerList(): List<AlarmTimer> {
        Timber.d("Getting parent timers")
        return withContext(viewModelScope.coroutineContext) {
            alarmTimerViewModel.getParentAlarmTimers()
        }
    }

}