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

    val parentAlarmTimers by lazy {
        return@lazy getParentAlarmTimerList()
    }

    private fun getParentAlarmTimerList(): List<AlarmTimer> {
        val list = mutableListOf<AlarmTimer>()

        viewModelScope.launch {
            list += alarmRepository.getParentTimersList()
        }

        return list
    }

    private suspend fun getParentAlarmTimerFlow(): Flow<AlarmTimer> {
        return alarmRepository.getParentTimersFlow()
    }
}