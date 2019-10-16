package commanderpepper.advancetimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.fragments.AlarmList
import timber.log.Timber

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        Timber.d("I'm alive!")

        val alarmList = AlarmList.newInstance("A", "B")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.entry_fragment_placeholder, alarmList)
        fragmentTransaction.commit()
    }
}
