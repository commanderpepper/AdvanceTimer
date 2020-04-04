package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import commanderpepper.advancetimer.ui.dismisstimer.DismissTimer
import commanderpepper.advancetimer.viewmodel.TIMER_ID
import timber.log.Timber

val DISMISS_TIMER_ID = "DISMISS_TIMER_ID"

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Timber.d("It works!")
//        val intExtra = intent.getIntExtra(TIMER_ID, -1)
        val longExtra = intent.getLongExtra(TIMER_ID, -1L)
        Timber.d("$longExtra")

        val activityIntent = Intent(context, DismissTimer::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        activityIntent.putExtra(DISMISS_TIMER_ID, longExtra)
        context.startActivity(activityIntent)
    }
}
