package commanderpepper.advancetimer.ui.entry

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import commanderpepper.advancetimer.R

import kotlinx.android.synthetic.main.activity_alarm_test.*
import timber.log.Timber

class AlarmActivityTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_test)
        setSupportActionBar(toolbar)

        Timber.d("The Alarm worked.")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
