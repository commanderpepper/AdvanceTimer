package commanderpepper.advancetimer.alarmcreation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.*
import commanderpepper.App
import commanderpepper.advancetimer.receivers.DISMISS_TIMER_ID
import commanderpepper.advancetimer.receivers.MyReceiver
import commanderpepper.advancetimer.viewmodel.TIMER_ID
import commanderpepper.advancetimer.workmanager.AlarmWorker
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject


/**
 * This class creates alarms using WorkManager
 */

class AlarmCreator @Inject constructor(
    val context: Context
) {

    /**
     * Makes a one off alarm
     */
    fun makeOneOffAlarm(triggerAtMillis: Long = 0L, timerId: Int) {
        val request = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(Duration.ofMillis(triggerAtMillis))
            .setInputData(workDataOf(DISMISS_TIMER_ID to timerId))
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            timerId.toString(),
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    /**
     * Makes a repeating timer
     */
    fun makeRepeatingAlarm(triggerAtMillis: Long = 0L, timerId: Int) {
        val request = PeriodicWorkRequestBuilder<AlarmWorker>(Duration.ofMillis(triggerAtMillis))
            .setInitialDelay(Duration.ofMillis(triggerAtMillis))
            .setInputData(workDataOf(DISMISS_TIMER_ID to timerId))
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            timerId.toString(),
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    /**
     * Cancels a timer scheduled with Work Manager
     */
    fun cancelTimer(timerId: Int){
        WorkManager.getInstance(context).cancelUniqueWork(timerId.toString())
    }
}