package commanderpepper.advancetimer.ui.alarmtimerdetail

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.databinding.FragmentAlarmTimerDetailBinding
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.alarmtimernew.PARENT_KEY
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

val DETAIL_TIMER_KEY = "alarmTimerId"
val FAB_KEY = "showAddTimerFAB"

class AlarmTimerDetailFragment : Fragment(), DeleteDialog.DeleteDialogListener {

    private val viewModel: AlarmTimerDetailViewModel by viewModels()

    private lateinit var binding: FragmentAlarmTimerDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_timer_detail, container, false)
        binding.vm = viewModel
        return binding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailCreateAlarmtimerFab.setOnClickListener {
            val bundle = bundleOf(PARENT_KEY to getAlarmTimerId())
            view.findNavController()
                .navigate(R.id.action_alarmTimerDetail_to_alarmTimerNew, bundle)
        }

        /**
         * Retrieve the title of the timer and use it to update the title view.
         */
        lifecycleScope.launch {
            val title = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveTimer(getAlarmTimerId()).title
            }
            binding.detailTimerTitle.text = getString(R.string.timerTitleDetail, title)
        }

        /**
         * Retrieve the name of the parent timer and update the parent title view.
         */
        lifecycleScope.launch {
            binding.detailParentTimerTitle.text = withContext(lifecycleScope.coroutineContext) {
                "Parent timer: " + viewModel.retrieveParentTimerTitle(getAlarmTimerId())
            }
        }

        /**
         * Populate a list of child timers.
         */
        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(getAlarmTimerId())
            Timber.d(childTimers.toString())

            val adapter = AlarmTimerAdapter(childTimers, NavGraphAction(R.id.action_alarmTimerDetail_self))

            val manager = LinearLayoutManager(this@AlarmTimerDetailFragment.context)

            val dividerItemDecoration =
                DividerItemDecoration(this@AlarmTimerDetailFragment.context, manager.orientation)

            binding.detailChildTimerList.adapter = adapter
            binding.detailChildTimerList.layoutManager = manager
            binding.detailChildTimerList.addItemDecoration(dividerItemDecoration)
        }

        /**
         * Turn off a timer.
         */
        binding.turnOffTimer.setOnClickListener {
            lifecycleScope.launch {
                Timber.d("Alarm to disable ${getAlarmTimerId()}")
                viewModel.stopTimer(getAlarmTimerId())
            }
            Toast.makeText(this@AlarmTimerDetailFragment.requireContext(), "Timer turned off", Toast.LENGTH_SHORT).show()
        }

        /**
         * Turn on a timer.
         */
        binding.turnOnTimer.setOnClickListener {
            lifecycleScope.launch {
                Timber.d("Alarm to enable")
                viewModel.restartTimer(getAlarmTimerId())
            }
            Toast.makeText(this@AlarmTimerDetailFragment.requireContext(), "Timer turned on", Toast.LENGTH_SHORT).show()
        }

        /**
         * Delete a timer
         */
        binding.deleteTimer.setOnClickListener {
            val deleteDialog = DeleteDialog()
            deleteDialog.setTargetFragment(this, 0)
            deleteDialog.show(parentFragmentManager, "Dismiss")
        }

        setTurnOnButtonText()
    }

    /**
     * If the timer is on, set the turn on button to state restart. If not, then set the timer to Turn On.
     */
    private fun setTurnOnButtonText() {
        lifecycleScope.launch {
            binding.turnOnTimer.text =
                if (viewModel.timerIsEnabled(getAlarmTimerId())) "Restart" else "Turn On"
        }
    }

    /**
     * Get alarm timer id passed to this fragment.
     */
    private fun getAlarmTimerId(): Int {
        return arguments?.getInt(DETAIL_TIMER_KEY) ?: -1
    }

    @ExperimentalStdlibApi
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        lifecycleScope.launch {
            viewModel.deleteTimer(getAlarmTimerId())
            Toast.makeText(this@AlarmTimerDetailFragment.requireContext(), "Timer deleted", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

}
