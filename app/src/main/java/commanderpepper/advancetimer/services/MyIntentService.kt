package commanderpepper.advancetimer.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import android.R
import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.content.Context
import commanderpepper.advancetimer.ui.MainActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class MyIntentService : IntentService("MyIntentService") {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate() {
        super.onCreate()

        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, "TEST")
            .setSmallIcon(commanderpepper.advancetimer.R.drawable.ic_launcher_background)
            .setContentTitle("My Awesome App")
            .setContentText("Doing some work...")
            .setContentIntent(pendingIntent).build()

        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {
        val string = intent?.getStringExtra("TEST") ?: "Meow"
        Log.d("TEST", string)

        serviceScope.launch {
            delay(50000)
            val sstring = intent?.getStringExtra("TEST") ?: "Meow"
            Log.d("TEST", "Whahey $sstring")
        }


        Toast.makeText(applicationContext, string, Toast.LENGTH_LONG).show()
    }

}
