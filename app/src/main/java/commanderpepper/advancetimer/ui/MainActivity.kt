package commanderpepper.advancetimer.ui

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.services.MyIntentService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(commanderpepper.advancetimer.R.layout.activity_main)

        val intent = Intent(this, MyIntentService::class.java)
        intent.putExtra("TEST", "Woof")
        startService(intent)
    }
}
