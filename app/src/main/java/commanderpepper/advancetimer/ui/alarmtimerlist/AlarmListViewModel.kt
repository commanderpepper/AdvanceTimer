package commanderpepper.advancetimer.ui.alarmtimerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AlarmListViewModel : ViewModel() {
    private val alarmRepository = AlarmRepository.get()
    private val alarmCreator = AlarmCreator.get()
    private val alarmTimerViewModel = AlarmTimerViewModel(alarmRepository,alarmCreator)

    val parentAlarmTimers by lazy {
        return@lazy getParentAlarmTimerList()
    }

    private fun getParentAlarmTimerList(): List<AlarmTimer> {
        val list = mutableListOf<AlarmTimer>()

        viewModelScope.launch {
            list += alarmTimerViewModel.getParentAlarmTimers()
        }

        return list
    }

    private suspend fun getParentAlarmTimerFlow(): Flow<AlarmTimer> {
        return alarmRepository.getParentTimersFlow()
    }
}