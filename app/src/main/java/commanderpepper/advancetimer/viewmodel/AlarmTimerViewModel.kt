package commanderpepper.advancetimer.viewmodel

import androidx.lifecycle.LiveData
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class AlarmTimerViewModel(
    private val alarmRepository: AlarmRepository,
    private val alarmCreator: AlarmCreator
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    fun getParentAlarmTimers(): List<AlarmTimer> {

        val list = mutableListOf<AlarmTimer>()

        scope.launch {
            withContext(scope.coroutineContext) {
                alarmRepository.getParentTimers().collect {
                    list.add(it)
                }
            }
        }

        return list
    }

}