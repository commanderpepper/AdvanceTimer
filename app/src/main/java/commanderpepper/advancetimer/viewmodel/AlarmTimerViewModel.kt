package commanderpepper.advancetimer.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.receivers.AlarmReceiver
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class AlarmTimerViewModel(
    private val alarmRepository: AlarmRepository,
    private val alarmCreator: AlarmCreator
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    fun getParentAlarmTimers(): List<AlarmTimer> {

        val list = mutableListOf<AlarmTimer>()

        scope.launch {
            withContext(scope.coroutineContext) {
                alarmRepository.getParentTimersFlow().collect {
                    list.add(it)
                }
            }
        }

        return list
    }

    fun addOneOffParentAlarm(context: Context, triggerAtMillis: Long) {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeAlarm(sourceIntent, triggerAtMillis)
        val alarmTimer = AlarmTimer(
            "Test",
            AlarmTimerType.OneOffAlarm,
            true,
            RequestCodeGenerator.get().getCurrentRequestCode(),
            null
        )
        scope.launch {
            alarmRepository.insertAlarmTimer(alarmTimer)
        }
    }

}