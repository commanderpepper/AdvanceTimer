package commanderpepper.advancetimer.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.receivers.AlarmReceiver
import commanderpepper.advancetimer.receivers.MyReceiver
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import timber.log.Timber
import javax.inject.Inject

const val TIMER_ID = "TIMER_ID"

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

    suspend fun getChildAlarmTimers(parentId: Int): List<AlarmTimer> {
        return withContext(scope.coroutineContext) {
            alarmRepository.getChildrenTimers(parentId).toList()
        }
    }

    fun addOneOffParentTimer(context: Context, triggerAtMillis: Long) {
        val testAlarmTimer = AlarmTimer(
            "test",
            AlarmTimerType.OneOffAlarm,
            true,
            0,
            alarmCreator.getRequestCode(),
            null
        )
        val id = testAlarmTimer.id
        val sourceIntent = Intent(context, MyReceiver::class.java)
        sourceIntent.putExtra(TIMER_ID, id)
        alarmCreator.makeTimer(context, sourceIntent, triggerAtMillis)
        scope.launch {
            alarmRepository.insertAlarmTimer(testAlarmTimer)
        }
    }

    fun makeTimerUsingContext(context: Context, triggerAtMillis: Long) {
        val sourceIntent = Intent(context, MyReceiver::class.java)
        alarmCreator.makeTimerUsingContext(context, sourceIntent, triggerAtMillis)
        scope.launch {
            val testAlarmTimer = AlarmTimer(
                "test",
                AlarmTimerType.OneOffAlarm,
                true,
                0,
                alarmCreator.getRequestCode(),
                null
            )
            alarmRepository.insertAlarmTimer(testAlarmTimer)
        }
    }

    fun createTimer(title: String, context: Context, triggerAtMillis: Long) {
        val testAlarmTimer = AlarmTimer(
            title,
            AlarmTimerType.OneOffTimer,
            true,
            triggerAtMillis,
            alarmCreator.getRequestCode(),
            null
        )
        val id = testAlarmTimer.id
        val sourceIntent = Intent(context, MyReceiver::class.java)
        sourceIntent.putExtra(TIMER_ID, id)

        alarmCreator.makeTimerUsingContext(context, sourceIntent, triggerAtMillis)
        scope.launch {
            alarmRepository.insertAlarmTimer(testAlarmTimer)
        }
    }

    fun createTimerWaitForId(title: String, context: Context, triggerAtMillis: Long) {
        scope.launch {
            val testAlarmTimer = AlarmTimer(
                title,
                AlarmTimerType.OneOffTimer,
                true,
                triggerAtMillis,
                alarmCreator.getRequestCode(),
                null
            )
            val insertedId = withContext(scope.coroutineContext) {
                alarmRepository.insertAlarmTimerGetId(testAlarmTimer)
            }
            val id = insertedId
            val sourceIntent = Intent(context, MyReceiver::class.java)
            sourceIntent.putExtra(TIMER_ID, id)

            alarmCreator.makeTimerUsingContext(context, sourceIntent, triggerAtMillis)
        }
    }
}

fun Activity.dismissKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText)
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, /*flags:*/ 0)
}