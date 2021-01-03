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
 * This class creates alarms and maybe saves some of the data to Room
 */

class AlarmCreator @Inject constructor(
    val context: Context
) {

    private val requestCodeGenerator = (context as App).appComponent.requestCodeGenerator()

    /**
     * Makes a one off alarm with the alarm manager and starts it.
     */
//    fun makeOneOffAlarm(
//        timerRequestCode: Int,
//        timerId: Int,
//        triggerAtMillis: Long
//    ) {
//        val intent = createIntentForNewAlarm(timerId, context)
//
//        val pendingIntent = createPendingIntentForNewAlarm(context, timerRequestCode, intent)
//
//        val alarmManager: AlarmManager =
//            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
//    }

    /**
     * Makes a one off alarm with Work Manager
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

    fun makeRepeatingAlarm(triggerAtMillis: Long = 0L, repeatAtMillis: Long, timerId: Int) {
        val request = PeriodicWorkRequestBuilder<AlarmWorker>(Duration.ofMillis(repeatAtMillis))
            .setInitialDelay(Duration.ofMillis(triggerAtMillis))
            .setInputData(workDataOf(DISMISS_TIMER_ID to timerId))
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            timerId.toString(),
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelTimer(timerId: Int){
        WorkManager.getInstance(context).cancelUniqueWork(timerId.toString())
    }

    /**
     * Makes a repeating alarm with the alarm manager and starts it.
     */
//    fun makeRepeatingAlarm(
//        timerRequestCode: Int,
//        timerId: Int,
//        triggerAtMillis: Long,
//        intervalAtMillis: Long
//    ) {
//        val intent = createIntentForNewAlarm(timerId, context)
//
//        val pendingIntent = createPendingIntentForNewAlarm(context, timerRequestCode, intent)
//
//        val alarmManager: AlarmManager =
//            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            triggerAtMillis,
//            intervalAtMillis,
//            pendingIntent
//        )
//    }

    fun getRequestCode() = requestCodeGenerator.getRequestCode()

    /**
     * Cancel a timer's pending intent and it's lock in the alarm manager.
     */
    fun cancelTimer(
        timerId: Long,
        timerRequestCode: Int
    ) {

        Timber.d("Request code when cancelling $timerRequestCode")

        val intent = Intent(context, MyReceiver::class.java)
        intent.putExtra(TIMER_ID, timerId)

        Timber.d("Intent to be cancelled, ${intent.type}")

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context, timerRequestCode, intent, PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            Timber.d("Cancel pending intent, ${pendingIntent.creatorPackage} , ${pendingIntent.creatorUid} , ${pendingIntent.creatorUserHandle} , ${pendingIntent.intentSender}")
            alarmManager.cancel(pendingIntent)
        } else {
            Timber.d("Cancelled pending intent is null")
        }
    }

    private fun createIntentForNewAlarm(timerId: Int, context: Context): Intent {
        return Intent(context, MyReceiver::class.java).apply {
            putExtra(TIMER_ID, timerId)
        }
    }

    private fun createPendingIntentForNewAlarm(
        context: Context,
        requestCode: Int,
        intent: Intent
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}