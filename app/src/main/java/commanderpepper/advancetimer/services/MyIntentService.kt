package commanderpepper.advancetimer.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import commanderpepper.advancetimer.ui.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class MyIntentService : IntentService("MyIntentService") {

    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                ""
            }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(commanderpepper.advancetimer.R.mipmap.ic_launcher)
            .setContentTitle("My Awesome App")
            .setContentText("Doing some work...")
            .setContentIntent(pendingIntent).build()

        startForeground(1, notification)
    }

    override fun onHandleIntent(intent: Intent?) {
        val string = intent?.getStringExtra("TEST") ?: "Meow"
        Timber.d(string)

        serviceScope.launch {
            delay(10000)
            val sstring = intent?.getStringExtra("TEST") ?: "Meow"
            withContext(Dispatchers.Main)
            {
                Timber.d("Whahey $sstring")
                Toast.makeText(applicationContext, string, Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

}
