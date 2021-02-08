package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.databinding.FragmentAlarmTimerNewBinding
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.UnitsOfTime
import commanderpepper.advancetimer.room.AlarmTimerType
import commanderpepper.advancetimer.ui.alarmtimerdetail.DETAIL_TIMER_KEY
import commanderpepper.advancetimer.viewmodel.dismissKeyboard
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PARENT_KEY = "parentId"

class AlarmTimerNewFragment : Fragment() {

    private val alarmTimerViewModel: AlarmTimerNewViewModel by viewModels()
    private lateinit var binding : FragmentAlarmTimerNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_timer_new, container, false)
        binding.viewmodel = alarmTimerViewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Set input values to default when the user pressed the back button
         */
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isEnabled){
                    isEnabled = false
                    findNavController().popBackStack()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the number picker amount and when the user changes the value update the view model
        binding.oneOffTimerNumber.hourNumber.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerHour = UnitsOfTime.Hour(progress.toLong())
            }
        }
        binding.oneOffTimerNumber.minuteNumber.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerMinute = UnitsOfTime.Minute(progress.toLong())
            }
        }
        binding.oneOffTimerNumber.secondNumber.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerSecond = UnitsOfTime.Second(progress.toLong())
            }
        }

        /**
         * If a child timer is being created, set the radio text to the two child timer options.
         */
        if (getParentId() != null) {
            binding.immediateStart.text = "Starts when parent starts"
            binding.delayedStart.text = "Starts when parent ends"
            alarmTimerViewModel.updateTimerStart(TimerStart.ParentStart)
        }

        /**
         * Set the alarm timer type in the view model whenever the user switches types.
         */
        binding.timerTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            val alarmTimerType: AlarmTimerType =
                if (i == R.id.oneOffRadioButton) AlarmTimerType.OneOffTimer else AlarmTimerType.RepeatingTimer
            alarmTimerViewModel.updateAlarmTimerType(alarmTimerType)

//            alarmTimerViewModel.updateRepeatVisibility(alarmTimerType == AlarmTimerType.RepeatingTimer )
        }

        /**
         * Set the timer start in the view model whenever the user switches start type.
         */
        binding.timerStartGroup.setOnCheckedChangeListener { _, i ->
            val timerStart =
                if (getParentId() != null) {
                    if (i == R.id.immediateStart)
                        TimerStart.ParentStart
                    else
                        TimerStart.ParentEnd
                } else {
                    if (i == R.id.immediateStart)
                        TimerStart.Immediate
                    else
                        TimerStart.Delayed
                }

            alarmTimerViewModel.updateTimerStart(timerStart)

        }

        binding.addAlarmtimer.setOnClickListener { saveButton ->
            /**
             * If the user didn't change the title, a generic title is generated.
             */
            alarmTimerViewModel.alarmTimerTitle =
                if (binding.timerTitle.text.toString() == resources.getString(R.string.alarmtimer_title_hint)) {
                    alarmTimerViewModel.createGenericTitle()
                } else {
                    binding.timerTitle.text.toString()
                }

            lifecycleScope.launch {
                /**
                 * Get a Flow from the view model. This helps with waiting for the insertion of timer data.
                 */
                val resultFlow = withContext(Dispatchers.Default) {
                    alarmTimerViewModel.createTimer(getParentId())
                }

                resultFlow.flowOn(Dispatchers.Main).collect { _ ->
                    requireActivity().dismissKeyboard()

                    // If there is not parent ID, then a new parent timer was created, the user will be sent back to the timer list fragment.
                    if (getParentId() == null) {
                        view.findNavController()
                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
                    } else {
                        val bundle = bundleOf(DETAIL_TIMER_KEY to getParentId()!!)

                        // Send the user back to the parent timer of the new timer created.
                        view.findNavController().navigate(
                            R.id.action_alarmTimerNew_to_alarmTimerDetail,
                            bundle
                        )
                    }
                }
            }
        }
    }

    private fun getParentId(): Int? {
        return arguments?.getInt(PARENT_KEY)
    }

}
