package commanderpepper.advancetimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import commanderpepper.advancetimer.R
import timber.log.Timber

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        Timber.d("I'm alive!")
    }
}
