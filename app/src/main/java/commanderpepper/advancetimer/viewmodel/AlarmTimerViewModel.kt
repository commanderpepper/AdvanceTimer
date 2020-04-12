package commanderpepper.advancetimer.viewmodel

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.model.UnitsOfTime
import commanderpepper.advancetimer.model.getTriggerTime
import commanderpepper.advancetimer.receivers.MyReceiver
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import timber.log.Timber
import javax.inject.Inject

const val TIMER_ID = "TIMER_ID"

/**
 * This is the general view model. It handles alarm creation, storage and deletion.
 * It interacts with the the alarm creator and alarm repository.
 * The UI view models will interact with this view model.
 */
class AlarmTimerViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmCreator: AlarmCreator
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    suspend fun getAlarmTimer(primaryId: Int): AlarmTimer {
        return withContext(scope.coroutineContext) {
            alarmRepository.getAlarmTimer(primaryId)
        }
    }

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

    /**
     * Get a list of child timers.
     */
    suspend fun getChildAlarmTimers(parentId: Int): List<AlarmTimer> {
        return withContext(scope.coroutineContext) {
            alarmRepository.getChildrenTimers(parentId).toList()
        }
    }

    /**
     * Create a timer and return a Flow<Int> where the value inside the Flow is the alarm ID just created.
     */
    suspend fun createTimer(
        title: String,
        triggerAtMillis: UnitsOfTime.MilliSecond,
        parentId: Int?,
        alarmTimerType: AlarmTimerType,
        intervalAtMillis: UnitsOfTime.MilliSecond
    ): Flow<Int> {

        val requestCode = alarmCreator.getRequestCode()

        Timber.d("Request code used to create database alarm $requestCode")

        val testAlarmTimer = AlarmTimer(
            title,
            alarmTimerType,
            true,
            triggerAtMillis.amount,
            intervalAtMillis.amount,
            requestCode,
            parentId
        )

        val insertedId = withContext(scope.coroutineContext) {
            alarmRepository.insertAlarmTimerGetId(testAlarmTimer)
        }

        when (alarmTimerType) {
            AlarmTimerType.OneOffTimer -> alarmCreator.makeOneOffAlarm(
                requestCode,
                insertedId,
                triggerAtMillis.amount
            )
            AlarmTimerType.RepeatingTimer -> alarmCreator.makeRepeatingAlarm(
                requestCode,
                insertedId,
                triggerAtMillis.amount,
                intervalAtMillis.amount
            )
        }

        return flow {
            emit(insertedId.toInt())
        }
    }

    /**
     * Modify the enabled / disabled state of an alarm
     */
    suspend fun modifyAlarmTimer(alarmTimerId: Int) {
        scope.launch {
            alarmRepository.modifyAlarmTimerEnabledState(alarmTimerId)
        }
    }

    /**
     * Re-enable an alarm
     */
    suspend fun enableAlarmTimer(alarmTimerId: Int) {
        val alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)
        val oldTrigger
        val newTriggerTime : UnitsOfTime.MilliSecond = getTriggerTime()
        when (alarmTimer.type) {
            is AlarmTimerType.OneOffTimer -> alarmCreator.makeOneOffAlarm(alarmTimer.requestCode, )
            is AlarmTimerType.RepeatingTimer ->
        }
        alarmRepository.enableAlarmTimer(alarmTimerId)
    }

    /**
     * Disable an alarm
     */
    suspend fun disableAlarmTime(alarmTimerId: Int) {
        alarmRepository.disableAlarmTimer(alarmTimerId)
        val alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)
        alarmCreator.cancelTimer(alarmTimer.id.toLong(), alarmTimer.requestCode)
    }
}

fun Activity.dismissKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText)
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, /*flags:*/ 0)
}