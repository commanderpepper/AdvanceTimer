package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Timber.d("Is this working?")
    }
}
