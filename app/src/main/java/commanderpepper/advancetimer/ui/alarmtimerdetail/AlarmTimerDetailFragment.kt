package commanderpepper.advancetimer.ui.alarmtimerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.alarmtimernew.PARENT_KEY
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

val DETAIL_TIMER_KEY = "alarmTimerId"
val FAB_KEY = "showAddTimerFAB"

class AlarmTimerDetailFragment : Fragment(), DeleteDialog.DeleteDialogListener {

    private val viewModel: AlarmTimerDetailViewModel by activityViewModels()
    private lateinit var detailTimerTitleView: TextView
    private lateinit var parentDetailTimerTitleView: TextView
    private lateinit var childTimersRecyclerView: RecyclerView
    private lateinit var addTimerFab: FloatingActionButton

    private lateinit var turnOnButton: Button
    private lateinit var turnOffButton: Button
    private lateinit var deleteButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_timer_detail, container, false)
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTimerTitleView = view.findViewById(R.id.detailTimerTitle)
        parentDetailTimerTitleView = view.findViewById(R.id.detailParentTimerTitle)
        childTimersRecyclerView = view.findViewById(R.id.detailChildTimerList)
        addTimerFab = view.findViewById(R.id.detail_create_alarmtimer_fab)
        turnOnButton = view.findViewById(R.id.turnOnTimer)
        turnOffButton = view.findViewById(R.id.turnOffTimer)
        deleteButton = view.findViewById(R.id.deleteTimer)

        /**
         * If the Floating Action Button enabled then add an onClickListener to the Floating Action Button.
         */
        if (getAddFabStatus()) {
            addTimerFab.setOnClickListener {
                val bundle = bundleOf(PARENT_KEY to getAlarmTimerId())
                view.findNavController()
                    .navigate(R.id.action_alarmTimerDetail_to_alarmTimerNew, bundle)
            }
        } else {
            /**
             * When the Floating Action Button is disabled, assume that we are entering from a broadcast and therefore modify the timer state and hide the Floating Action Button.
             */
            addTimerFab.hide()
            lifecycleScope.launch {
                withContext(lifecycleScope.coroutineContext) {
                    viewModel.modifyEnabledState(getAlarmTimerId())
                }
            }
        }

        /**
         * Retrieve the title of the timer and use it to update the title view.
         */
        lifecycleScope.launch {
            val title = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveTimer(getAlarmTimerId()).title
            }
            detailTimerTitleView.text = getString(R.string.timerTitleDetail, title)
        }

        /**
         * Retrieve the name of the parent timer and update the parent title view.
         */
        lifecycleScope.launch {
            parentDetailTimerTitleView.text = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveParentTimerTitle(getAlarmTimerId())
            }
        }

        /**
         * Populate a list of child timers.
         */
        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(getAlarmTimerId())

            /**
             * Set up the adapter for the recycler view. If the FAB is disabled then so is the ability to go to child timers.
             */
            val adapter =
                AlarmTimerAdapter(
                    childTimers,
                    if (getAddFabStatus()) {
                        NavGraphAction(R.id.action_alarmTimerDetail_self)
                    } else {
                        //Set the button to turn on. This makes sure the text is set when entering this fragment from a broadcast receiver.
                        setTurnOnButtonText()
                        NavGraphAction(
                            0
                        )
                    }
                )

            val manager = LinearLayoutManager(this@AlarmTimerDetailFragment.context)

            val dividerItemDecoration =
                DividerItemDecoration(this@AlarmTimerDetailFragment.context, manager.orientation)

            childTimersRecyclerView.adapter = adapter
            childTimersRecyclerView.layoutManager = manager
            childTimersRecyclerView.addItemDecoration(dividerItemDecoration)

        }

        /**
         * Turn off a timer.
         */
        turnOffButton.setOnClickListener {
            lifecycleScope.launch {
                Timber.d("Alarm to disable ${getAlarmTimerId()}")
                viewModel.stopTimer(getAlarmTimerId())
            }
        }

        /**
         * Turn on a timer.
         */
        turnOnButton.setOnClickListener {
            lifecycleScope.launch {
                Timber.d("Alarm to enable")
                viewModel.restartTimer(getAlarmTimerId())
            }
        }

        /**
         * Delete a timer
         */
        deleteButton.setOnClickListener {
//                viewModel.deleteTimer(getAlarmTimerId())
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
            turnOnButton.text =
                if (viewModel.timerIsEnabled(getAlarmTimerId())) "Restart" else "Turn On"
        }
    }

    /**
     * Get alarm timer id passed to this fragment.
     */
    private fun getAlarmTimerId(): Int {
        return arguments?.getInt(DETAIL_TIMER_KEY) ?: -1
    }

    /**
     * Get FAB status argument passed to this fragment, I assume it to be true.
     */
    private fun getAddFabStatus(): Boolean {
        return arguments?.getBoolean(FAB_KEY, true) ?: true
    }

    @ExperimentalStdlibApi
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        lifecycleScope.launch {
            viewModel.deleteTimer(getAlarmTimerId())
            findNavController().popBackStack()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

}
