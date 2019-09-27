package commanderpepper.advancetimer.ui

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.services.TestService
import android.R
import androidx.core.app.NotificationCompat
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(commanderpepper.advancetimer.R.layout.activity_main)

        val pendingIntent: PendingIntent =
            Intent(this, TestService::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

//        val notification: Notification = Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
//            .setContentTitle(getText(R.string.notification_title))
//            .setContentText(getText(R.string.notification_message))
//            .setSmallIcon(R.drawable.icon)
//            .setContentIntent(pendingIntent)
//            .setTicker(getText(R.string.ticker_text))
//            .build()

        val notification = NotificationCompat.Builder(this, "1")
            .setContentTitle("Foreground Service")
            .setContentText("This is a test")
            .setSmallIcon(commanderpepper.advancetimer.R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()

//        startService(pendingIntent)
    }
}
