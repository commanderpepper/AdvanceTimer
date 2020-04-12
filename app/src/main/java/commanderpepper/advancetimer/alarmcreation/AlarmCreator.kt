package commanderpepper.advancetimer.alarmcreation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import commanderpepper.App
import commanderpepper.advancetimer.receivers.MyReceiver
import commanderpepper.advancetimer.viewmodel.TIMER_ID
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
        timerRequestCode: Int,
        timerId: Long,
        triggerAtMillis: Long
    ) {
        val intent = Intent(context, MyReceiver::class.java)
        intent.putExtra(TIMER_ID, timerId)

        Timber.d("Intent created, ${intent.action}")

        Timber.d("Creating alarm with request code: $timerRequestCode")

        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                timerRequestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

        Timber.d("Creating pending intent, ${pendingIntent.creatorPackage} , ${pendingIntent.creatorUid} , ${pendingIntent.creatorUserHandle} , ${pendingIntent.intentSender}")

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    /**
     * Makes a repeating alarm with the alarm manager.
     */
    fun makeRepeatingAlarm(
        timerRequestCode: Int,
        timerId: Long,
        triggerAtMillis: Long,
        intervalAtMillis: Long
    ) {

        Timber.d("Alarm creation ")

        val intent = Intent(context, MyReceiver::class.java)
        intent.putExtra(TIMER_ID, timerId)

        Timber.d("Intent created, ${intent.action}")

        Timber.d("Creating alarm with request code: $timerRequestCode")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            timerRequestCode,
            intent,
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

    fun enableAlarm(timerId: Long, timerRequestCode: Int){
        val intent = Intent(context, MyReceiver::class.java)
        intent.putExtra(TIMER_ID, timerId)

        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent = PendingIntent.getBroadcast(
            context, timerRequestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT
        )


    }
}