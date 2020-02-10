package commanderpepper.advancetimer.ui.alarmlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlarmListViewModel : ViewModel() {
    private val alarmRepository = AlarmRepository.get()

    suspend fun getParentAlarmTimerList(): List<AlarmTimer> {
        return alarmRepository.getParentTimersList()
    }

    suspend fun getParentAlarmTimerFlow(): Flow<AlarmTimer> {
        return alarmRepository.getParentTimersFlow()
    }
}