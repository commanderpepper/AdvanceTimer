package commanderpepper.advancetimer.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions and extra parameters.
 */
class MyIntentService : IntentService("MyIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val string = intent?.getStringExtra("TEST") ?: "Meow"
        Log.d("TEST", string)

        Toast.makeText(applicationContext, string, Toast.LENGTH_LONG).show()
    }

}
