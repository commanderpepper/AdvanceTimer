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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import timber.log.Timber

class AlarmTimerViewModel private constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmCreator: AlarmCreator
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    /**
     * Gets the list of parent alarm timers using this view model
     * coroutine context to ensure work is performed off the main thread
     */
    suspend fun getParentAlarmTimers(): List<AlarmTimer> {
        Timber.d("Getting parent timers")
        return withContext(scope.coroutineContext) {
            alarmRepository.getParentTimersFlow().toList()
        }
    }

    suspend fun getChildAlarmTimers(parentId: Int): List<AlarmTimer> {
        return withContext(scope.coroutineContext) {
            alarmRepository.getChildrenTimers(parentId).toList()
        }
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

    fun addOneOffParentTimer(context: Context, triggerAtMillis: Long) {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeTimer(sourceIntent, triggerAtMillis)
        scope.launch {
            val testAlarmTimer = AlarmTimer(
                "test",
                AlarmTimerType.OneOffAlarm,
                true,
                alarmCreator.getRequestCode(),
                null
            )
            alarmRepository.insertAlarmTimer(testAlarmTimer)
        }
    }

    companion object {
        private var INSTANCE: AlarmTimerViewModel? = null
        fun initialize(
            alarmRepository: AlarmRepository,
            alarmCreator: AlarmCreator
        ) {
            if (INSTANCE == null) INSTANCE = AlarmTimerViewModel(alarmRepository, alarmCreator)
        }

        fun get(): AlarmTimerViewModel {
            return INSTANCE ?: throw IllegalArgumentException("CrimeRepository must be initialized")
        }
    }

}