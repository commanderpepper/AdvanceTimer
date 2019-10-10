package commanderpepper.advancetimer.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.intents.AlarmReceiver
import commanderpepper.advancetimer.services.MyIntentService
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var makeNotifcationButton: Button
    private lateinit var alarmButton: Button

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(commanderpepper.advancetimer.R.layout.activity_main)

        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmIntent = Intent(this, MyIntentService::class.java).let { intent ->
//            PendingIntent.getBroadcast(this, 0, intent, 0)
//        }
        alarmIntent = Intent(this, AlarmManagerActivity::class.java).let {
            intent -> PendingIntent.getActivities(this, 0, arrayOf(intent), 0)
        }

        makeNotifcationButton = findViewById(R.id.create_button)
        alarmButton = findViewById(R.id.create_alarm)

        makeNotifcationButton.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            intent.putExtra("TESTA", "Woof")
            startService(intent)
        }

        alarmButton.setOnClickListener {
            val time = findViewById<EditText>(R.id.secondsInput).text.toString()
            if (time != ""){
                val seconds = time.toLong() * 1000
                Timber.tag("TIME").d(System.currentTimeMillis().toString())
                alarmMgr?.setExact(AlarmManager.RTC, System.currentTimeMillis() + seconds, alarmIntent)
            }
        }

//        val intent = Intent(this, MyIntentService::class.java)
//        intent.putExtra("TEST", "Woof")
//        startService(intent)
    }
}
