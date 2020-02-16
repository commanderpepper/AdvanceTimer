package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import timber.log.Timber

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        val fooString = intent.getStringExtra("MESSAGE") ?: "Alarm received"
        Timber.d(fooString)
        Toast.makeText(context, fooString, Toast.LENGTH_LONG).show()
    }
}