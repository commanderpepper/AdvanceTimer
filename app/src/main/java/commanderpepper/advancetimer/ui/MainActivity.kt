package commanderpepper.advancetimer.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.services.MyIntentService
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
        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        makeNotifcationButton = findViewById(R.id.create_button)

        makeNotifcationButton.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            intent.putExtra("TESTA", "Woof")
            startService(intent)
        }

        alarmButton.setOnClickListener {
            // Set the alarm to start at approximately 2:00 p.m.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 14)
            }

// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
            alarmMgr?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }

//        val intent = Intent(this, MyIntentService::class.java)
//        intent.putExtra("TEST", "Woof")
//        startService(intent)
    }
}
