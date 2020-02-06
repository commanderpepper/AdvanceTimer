package commanderpepper.advancetimer.ui.entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.alarmlist.AlarmListFragment
import timber.log.Timber

class EntryWorkflowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        Timber.d("I'm alive!")

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val alarmListFragment = AlarmListFragment()
        fragmentTransaction.replace(R.id.entry_fragment_placeholder, alarmListFragment)
        fragmentTransaction.commit()
    }
}
