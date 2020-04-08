package commanderpepper.advancetimer.alarmcreation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import commanderpepper.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject


/**
 * This class creates alarms and maybe saves some of the data to Room
 */

class AlarmCreator @Inject constructor(
    val context: Context
) {

    private val requestCodeGenerator = (context as App).appComponent.requestCodeGenerator()

    /**
     * Makes a one off alarm with the alarm manager.
     */
    fun makeOneOffAlarm(
        context: Context,
        intent: Intent,
        triggerAtMillis: Long
    ) {
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCodeGenerator.getRequestCode(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    /**
     * Makes a repeating alarm with the alarm manager.
     */
    fun makeRepeatingAlarm(
        context: Context,
        sourceIntent: Intent,
        triggerAtMillis: Long,
        intervalAtMillis: Long
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeGenerator.getRequestCode(),
            sourceIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalAtMillis,
            pendingIntent
        )
    }

    fun getRequestCode() = requestCodeGenerator.getCurrentRequestCode()

    /**
     * Cancel a timer's pending intent and it's lock in the alarm manager.
     */
    fun cancelTimer(context: Context, intent: Intent, timerRequestCode: Int) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context, timerRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}