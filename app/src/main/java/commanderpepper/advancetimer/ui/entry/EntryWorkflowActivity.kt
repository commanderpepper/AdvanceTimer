package commanderpepper.advancetimer.ui.entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.workflow.ui.ExperimentalWorkflowUi
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.fragments.AlarmList
import timber.log.Timber

@UseExperimental(ExperimentalWorkflowUi::class)
class EntryWorkflowActivity : AppCompatActivity() {

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
