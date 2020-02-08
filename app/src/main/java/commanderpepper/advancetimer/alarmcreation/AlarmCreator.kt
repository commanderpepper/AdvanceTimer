package commanderpepper.advancetimer.alarmcreation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * This class creates alarms and maybe saves some of the data to Room
 */

class AlarmCreator(private val context: Context){

    private val alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val requestCodeGenerator = RequestCodeGenerator.get()

    fun makeAlarm(intent: Intent, triggerAtMillis: Long) {
        val pendingIntent =
            creatingPendingIntent(intent, PendingIntent.FLAG_ONE_SHOT)
        createAlarm(pendingIntent, triggerAtMillis)
    }

    fun makeTimer(intent: Intent, triggerAtMillis: Long) {
        val pendingIntent =
            creatingPendingIntent(intent, PendingIntent.FLAG_ONE_SHOT)
        createTimer(pendingIntent, triggerAtMillis)
    }

    fun makeRepeatingAlarm(intent: Intent, triggerAtMillis: Long, intervalAtMillis: Long) {
        val pendingIntent =
            creatingPendingIntent(intent)
        createRepeatingAlarm(pendingIntent, triggerAtMillis, intervalAtMillis)
    }

    fun makeRepeatingTimer(intent: Intent, triggerAtMillis: Long, intervalAtMillis: Long) {
        val pendingIntent =
            creatingPendingIntent(intent)
        createRepeatingTimer(pendingIntent, triggerAtMillis, intervalAtMillis)
    }

    private fun creatingPendingIntent(
        intent: Intent,
        flag: Int = PendingIntent.FLAG_CANCEL_CURRENT
    ): PendingIntent {
        return PendingIntent.getActivity(
            context,
            requestCodeGenerator.getRequestCode(),
            intent,
            flag
        )
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