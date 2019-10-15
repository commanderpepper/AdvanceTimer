package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Is triggered when alarm goes off, i.e. receiving a system broadcast
        if (intent.action == "BASIC") {
            val fooString = intent.getStringExtra("MESSAGE")
            Toast.makeText(context, fooString, Toast.LENGTH_LONG).show()
        }
    }
}