package commanderpepper.advancetimer.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * This receiver is called after a system reboot is complete to instantiate alarms.
 * Specifically, when this broadcast is sent
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
 */
class RebootReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}