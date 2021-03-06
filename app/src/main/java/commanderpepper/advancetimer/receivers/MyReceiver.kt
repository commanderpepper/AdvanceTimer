package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import commanderpepper.advancetimer.ui.dismisstimer.DismissTimer
import commanderpepper.advancetimer.viewmodel.TIMER_ID
import timber.log.Timber

val DISMISS_TIMER_ID = "DISMISS_TIMER_ID"

class MyReceiver : BroadcastReceiver() {

    /**
     * Used to set the intent for the alarm manager.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val timerId = intent.getIntExtra(TIMER_ID, 1)
        Timber.d("$timerId")

        val activityIntent = Intent(context, DismissTimer::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        activityIntent.putExtra(DISMISS_TIMER_ID, timerId)
        context.startActivity(activityIntent)
    }
}
