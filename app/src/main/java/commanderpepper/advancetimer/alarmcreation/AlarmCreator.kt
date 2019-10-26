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

class AlarmCreator(val context: Context) : CoroutineScope by CoroutineScope(Dispatchers.IO){

    private val alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun makeAlarm(pendingIntent: PendingIntent, triggerAtMillis: Long){
        launch {
            createAlarm(pendingIntent, triggerAtMillis)
        }
    }

    private suspend fun createAlarm(pendingIntent: PendingIntent, triggerAtMillis: Long) {
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

}