package commanderpepper.advancetimer.ui.dismisstimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.receivers.DISMISS_TIMER_ID
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DismissTimer : AppCompatActivity() {

    private lateinit var viewModel: DismissTimerViewModel
    private lateinit var dismissTimerTitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(DismissTimerViewModel::class.java)
        setContentView(R.layout.activity_dismiss_timer)

        dismissTimerTitleTextView = findViewById(R.id.dismissTimerTitle)

        val dismissId = intent.getLongExtra(DISMISS_TIMER_ID, -1)

        Timber.d("$dismissId")

        if (dismissId > 0) {
            lifecycleScope.launch {
                dismissTimerTitleTextView.text =
                    withContext(lifecycleScope.coroutineContext) {
                        viewModel.retrieveTimer(dismissId.toInt()).title
                    }
            }
        }
    }
}
