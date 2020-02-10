package commanderpepper.advancetimer.ui.alarmlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.alarmtimeredit.AlarmTimerEditFragment

class AlarmListActivity : AppCompatActivity() {

    private lateinit var newAlarmTimerButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)

        newAlarmTimerButton = findViewById(R.id.create_alarmtimer_fab)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (currentFragment == null) {
            val alarmListFragment = AlarmListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, alarmListFragment)
                .commit()
        }

//        newAlarmTimerButton.setOnClickListener {
//            val alarmTimerEditFragment = AlarmTimerEditFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment, alarmTimerEditFragment)
//                .commit()
//        }
    }
}
