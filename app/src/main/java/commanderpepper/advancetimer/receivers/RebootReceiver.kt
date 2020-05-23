package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import commanderpepper.App
import kotlinx.coroutines.*
import timber.log.Timber

/**
 * This receiver is called after a system reboot is complete to instantiate alarms.
 * Specifically, when this broadcast is sent
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
 */
class RebootReceiver : BroadcastReceiver() {

    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Main + job)


    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.d("Inside Reboot Receiver")

        val application = context!!.applicationContext as App
        val viewModel = application.appComponent.alarmTimerViewModelGenerator()

        scope.launch {
            Timber.d(viewModel.getParentAlarmTimers().toString())
        }
    }

}