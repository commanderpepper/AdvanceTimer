package commanderpepper.advancetimer.alarmcreation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * This class creates alarms and maybe saves some of the data to Room
 */

class AlarmCreator(val context: Context) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun makeAlarm(intent: Intent, triggerAtMillis: Long) {
        launch {
            val pendingIntent =
                PendingIntent.getActivity(context, RequestCodeGenerator.getRequestCode(context), intent, 0)
            createAlarm(pendingIntent, triggerAtMillis)
        }
    }

    fun makeAlarm(pendingIntent: PendingIntent, triggerAtMillis: Long) {
        launch {
            createAlarm(pendingIntent, triggerAtMillis)
        }
    }

    fun makeTimer(pendingIntent: PendingIntent, triggerAtMillis: Long) {
        launch {
            createTimer(pendingIntent, triggerAtMillis)
        }
    }

    private fun createAlarm(pendingIntent: PendingIntent, triggerAtMillis: Long) {
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun createTimer(pendingIntent: PendingIntent, triggerAtMillis: Long) {
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun createRepeatingAlarm(
        pendingIntent: PendingIntent,
        triggerAtMillis: Long,
        intervalAtMillis: Long
    ) {
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalAtMillis,
            pendingIntent
        )
    }

    private fun createRepeatingTimer(
        pendingIntent: PendingIntent,
        triggerAtMillis: Long,
        intervalAtMillis: Long
    ) {
        alarmMgr.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerAtMillis,
            intervalAtMillis,
            pendingIntent
        )
    }
}