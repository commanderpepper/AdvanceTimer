package commanderpepper.advancetimer.ui.dismisstimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.receivers.DISMISS_TIMER_ID
import commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DismissTimer : AppCompatActivity() {

    private lateinit var viewModel: DismissTimerViewModel
    private lateinit var dismissTimerTitleTextView: TextView
    private lateinit var parentTimerTitleTextView: TextView
    private lateinit var childTimersRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(DismissTimerViewModel::class.java)
        setContentView(R.layout.activity_dismiss_timer)

        dismissTimerTitleTextView = findViewById(R.id.dismissTimerTitle)
        parentTimerTitleTextView = findViewById(R.id.parentTimerTitle)
        childTimersRecyclerView = findViewById(R.id.dismissChildTimerList)

        val dismissId = intent.getLongExtra(DISMISS_TIMER_ID, -1)

        Timber.d("$dismissId")

        if (dismissId > 0) {
            lifecycleScope.launch {
                val title = withContext(lifecycleScope.coroutineContext) {
                    viewModel.retrieveTimer(dismissId.toInt()).title
                }
                dismissTimerTitleTextView.text = "Title: $title"
            }
        }

        lifecycleScope.launch {
            parentTimerTitleTextView.text = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveParentTimerTitle(dismissId.toInt())
            }
        }

        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(dismissId.toInt())

            val adapter = AlarmTimerAdapter(childTimers)
            val manager = LinearLayoutManager(this@DismissTimer)

            val dividerItemDecoration = DividerItemDecoration(
                this@DismissTimer,
                manager.orientation
            )
            childTimersRecyclerView.adapter = adapter
            childTimersRecyclerView.layoutManager = manager
            childTimersRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }
}
