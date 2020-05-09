package commanderpepper.advancetimer.ui.dismisstimer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.App
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.receivers.DISMISS_TIMER_ID
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.alarmtimerdetail.AlarmTimerDetailFragment
import commanderpepper.advancetimer.ui.alarmtimerdetail.DETAIL_TIMER_KEY
import commanderpepper.advancetimer.ui.alarmtimerdetail.FAB_KEY
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DismissTimer : AppCompatActivity() {

    private lateinit var viewModel: AlarmTimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (application as App).appComponent.alarmTimerViewModelGenerator()

        setContentView(R.layout.activity_dismiss_timer)

        val dismissId = intent.getLongExtra(DISMISS_TIMER_ID, -1)

        lifecycleScope.launch {
            viewModel.enableParentEndChildTimers(dismissId.toInt())

            val fragment = AlarmTimerDismissFragment()
            val bundle = bundleOf(DETAIL_TIMER_KEY to dismissId.toInt())
//            bundle.putBoolean(FAB_KEY, false)

            fragment.arguments = bundle

            val manager = supportFragmentManager
            manager.beginTransaction()
                .replace(R.id.dismissDetailFragment, fragment)
                .commit()
        }
    }
}
