package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import commanderpepper.advancetimer.ui.dismisstimer.DismissTimer
import commanderpepper.advancetimer.ui.entry.AlarmActivityTest
import timber.log.Timber

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Timber.d("Is this working?")
        val intent = Intent(context, DismissTimer::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
