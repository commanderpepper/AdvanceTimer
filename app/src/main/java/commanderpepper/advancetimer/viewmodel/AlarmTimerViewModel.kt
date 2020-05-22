package commanderpepper.advancetimer.viewmodel

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.annotation.VisibleForTesting
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.UnitsOfTime
import commanderpepper.advancetimer.model.getTriggerTime
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
        triggerTime: UnitsOfTime.MilliSecond,
        parentId: Int?,
        alarmTimerType: AlarmTimerType,
        deltaTime: UnitsOfTime.MilliSecond,
        timerStart: TimerStart
    ): Flow<Int> {

        val requestCode = alarmCreator.getRequestCode()

        Timber.d("Request code used to create database alarm $requestCode")
        Timber.d("$timerStart")

        val testAlarmTimer = AlarmTimer(
            title,
            alarmTimerType,
            timerStart,
            timerStart is TimerStart.Immediate,
            deltaTime,
            triggerTime,
            requestCode,
            parentId
        )

        val insertedId = withContext(scope.coroutineContext) {
            alarmRepository.insertAlarmTimerGetId(testAlarmTimer)
        }

        alarmCreator.makeOneOffAlarm(
            requestCode, insertedId.toInt(), triggerTime.amount
        )

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

    suspend fun modifyTriggerTime(alarmTimerId: Int) {
        val alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)

        val newTriggerTime = getTriggerTime(alarmTimer.deltaTime)
        alarmRepository.modifyTriggerTime(alarmTimerId, newTriggerTime)
    }

    /**
     * Re-enable an alarm
     */
    suspend fun enableAlarmTimer(alarmTimerId: Int) {
        var alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)

        //If the timer is active, disable it before restarting it.
        if (alarmTimer.enabled) {
            disableAlarmTime(alarmTimerId)
        }

        val newTriggerTime = getTriggerTime(alarmTimer.deltaTime)
        // Set the timer status from false to true and modify the trigger time.
        alarmRepository.enableAlarmTimer(alarmTimerId, newTriggerTime)

        val alarmTimerType = alarmTimer.type

        alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)

        // Start the timer in the alarm creator
        startTimer(alarmTimerType, alarmTimer)

        // Enable all child timers scheduled to start when this timer begins
        enableParentStartChildTimers(alarmTimerId)
    }

    suspend fun renableAlarmTimer(alarmTimerId: Int) {
        val alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)

        if (alarmTimer.type == AlarmTimerType.RepeatingTimer) {
            enableAlarmTimer(alarmTimer.id)
        }
    }

    /**
     * Start a timer
     */
    private fun startTimer(
        alarmTimerType: AlarmTimerType,
        alarmTimer: AlarmTimer
    ) {
        alarmCreator.makeOneOffAlarm(
            alarmTimer.requestCode,
            alarmTimer.id,
            alarmTimer.triggerTime.amount
        )
    }

    private suspend fun enableParentStartChildTimers(parentId: Int) {
        val childTimers = alarmRepository.getChildrenTimers(parentId).toList()
            .filter { it.timerStart == TimerStart.ParentStart }
        if (childTimers.isEmpty()) {
            return
        } else {
            childTimers.forEach {
                enableAlarmTimer(it.id)
            }
        }
    }

    /**
     * Turn on all child timers that were scheduled to begin when their parent timer has ended.
     */
    suspend fun enableParentEndChildTimers(parentId: Int) {
        val childTimers = alarmRepository.getChildrenTimers(parentId).toList()
            .filter { it.timerStart == TimerStart.ParentEnd }
        if (childTimers.isEmpty()) {
            return
        } else {
            childTimers.forEach {
                enableAlarmTimer(it.id)
            }
        }
    }

    /**
     * Disable an alarm
     */
    suspend fun disableAlarmTime(alarmTimerId: Int) {
        alarmRepository.disableAlarmTimer(alarmTimerId)
        val alarmTimer = alarmRepository.getAlarmTimer(alarmTimerId)
        alarmCreator.cancelTimer(alarmTimer.id.toLong(), alarmTimer.requestCode)
    }

    suspend fun checkTimerStatus(alarmTimerId: Int): Boolean {
        return alarmRepository.getAlarmTimer(alarmTimerId).enabled
    }

    /**
     * Delete a timer, this will delete a timer and all its child timers.
     */
    @ExperimentalStdlibApi
    suspend fun deleteTimer(alarmTimerId: Int) {
        alarmRepository.getAllTimersRelatedToParentTimer(alarmTimerId).forEach {
            alarmCreator.cancelTimer(it.id.toLong(), it.requestCode)
        }
        alarmRepository.deleteTimer(alarmTimerId)
    }

    @VisibleForTesting
    fun setUpAlarmRepoForTesting() {
        alarmRepository.setDatabaseForTesting()
    }

}

fun Activity.dismissKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText)
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, /*flags:*/ 0)
}