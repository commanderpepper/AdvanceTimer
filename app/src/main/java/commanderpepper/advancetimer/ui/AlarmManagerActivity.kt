package commanderpepper.advancetimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import commanderpepper.advancetimer.R
import timber.log.Timber

class AlarmManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_manager)
        Timber.tag("TIME").d(System.currentTimeMillis().toString())
    }
}
