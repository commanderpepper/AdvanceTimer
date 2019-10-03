package commanderpepper.advancetimer.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationManager
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import android.app.Notification
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Binder
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import timber.log.Timber
import commanderpepper.advancetimer.services.LocalService.LocalBinder
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.app.PendingIntent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import commanderpepper.advancetimer.ui.MainActivity


/**
 * From the developers page on Services
 */

class LocalService : Service() {

    private var mNM: NotificationManager? = null

    private val NOTIFICATION =
        getString(commanderpepper.advancetimer.R.string.local_service_started)

    inner class LocalBinder : Binder() {
        internal val service: LocalService
            get() = this@LocalService
    }

    private val mBinder = LocalBinder()

    override fun onCreate() {
        mNM = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("LocalService Received start id $startId : $intent")
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mNM!!.cancel(NOTIFICATION.toInt())

        Toast.makeText(this, "It stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    private fun showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        val text = "Meow meow meow meow meow meow"

        // The PendingIntent to launch our activity if the user selects this notification
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0
        )

        // Set the info for the views that show in the notification panel.
        val notification = Notification.Builder(this)
            .setSmallIcon(R.drawable.btn_star)  // the status icon
            .setTicker(text)  // the status text
            .setWhen(System.currentTimeMillis())  // the time stamp
            .setContentTitle("A title alright")  // the label of the entry
            .setContentText(text)  // the contents of the entry
            .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
            .build()

        // Send the notification.
        mNM!!.notify(NOTIFICATION.toInt(), notification)
    }
}
