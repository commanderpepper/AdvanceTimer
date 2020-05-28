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

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    override fun onReceive(context: Context, intent: Intent) {
        val application = context.applicationContext as App
        val viewModel = application.appComponent.alarmTimerViewModelGenerator()

        scope.launch {
            viewModel.restartEnabledTimers()
        }
    }

}