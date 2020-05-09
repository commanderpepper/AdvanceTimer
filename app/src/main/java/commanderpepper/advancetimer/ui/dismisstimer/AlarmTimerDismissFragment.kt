package commanderpepper.advancetimer.ui.dismisstimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.alarmtimerdetail.DETAIL_TIMER_KEY
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch

class AlarmTimerDismissFragment : Fragment() {

    private val viewModel: AlarmTimerDismissViewModel by activityViewModels()

    private lateinit var dismissTimerTitle: TextView
    private lateinit var dismissParentTimerTitle: TextView
    private lateinit var dismissButton: Button
    private lateinit var dismissChildList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_timer_dismiss, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dismissTimerTitle = view.findViewById(R.id.dismissTimerTitle)
        dismissParentTimerTitle = view.findViewById(R.id.dismissParentTimerTitle)
        dismissButton = view.findViewById(R.id.dismissTimerButton)
        dismissChildList = view.findViewById(R.id.dismissChildTimerList)

        /**
         * Turn off a timer
         */
        lifecycleScope.launch {
            viewModel.modifyEnabledState(getAlarmTimerId())
        }

        /**
         * Set the timer title
         */
        lifecycleScope.launch {
            val title = viewModel.retrieveTimerTitle(getAlarmTimerId())
            dismissTimerTitle.text = getString(R.string.timerTitleDetail, title)
        }

        /**
         * Set parent timer title
         */
        lifecycleScope.launch {
            val title = "Parent timer: " + viewModel.retrieveParentTimerTitle(getAlarmTimerId())
            dismissParentTimerTitle.text = title
        }

        /**
         * Set up the adapter for the recycler view.
         */
        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(getAlarmTimerId())

            val adapter = AlarmTimerAdapter(childTimers, NavGraphAction(0))

            val manager = LinearLayoutManager(this@AlarmTimerDismissFragment.context)

            val dividerItemDecoration =
                DividerItemDecoration(this@AlarmTimerDismissFragment.context, manager.orientation)

            dismissChildList.adapter = adapter
            dismissChildList.layoutManager = manager
            dismissChildList.addItemDecoration(dividerItemDecoration)
        }
    }

    /**
     * Get alarm timer id passed to this fragment.
     */
    private fun getAlarmTimerId(): Int {
        return arguments?.getInt(DETAIL_TIMER_KEY) ?: -1
    }
}