package commanderpepper.advancetimer.ui.alarmtimerlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R

class AlarmListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_list)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        /**
         * If there is no fragment within the activity, instantiate the activity with the timer list fragment.
         */
        if (currentFragment == null) {
            val alarmListFragment = AlarmTimerListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment, alarmListFragment)
                .commit()
        }
    }
}
