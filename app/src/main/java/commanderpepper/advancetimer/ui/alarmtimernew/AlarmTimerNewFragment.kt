package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import it.sephiroth.android.library.numberpicker.setListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PARENT_KEY = "parentId"

class AlarmTimerNewFragment : Fragment() {

    private lateinit var saveButton: Button
    private val alarmTimerViewModel: AlarmTimerNewViewModel by activityViewModels()

    private lateinit var binding: FragmentAlarmTimerNewBinding

    private lateinit var alarmTimerTitle: EditText

    private lateinit var alarmTypeRadioGroup: RadioGroup
    private lateinit var timerStartGroup: RadioGroup

    private lateinit var triggerHourNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var triggerMinuteNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var triggerSecondNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker

    private lateinit var repeatHourNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var repeatMinuteNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var repeatSecondNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker

    private lateinit var immediateStartRadioButton: RadioButton
    private lateinit var delayedStartRadioButton: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_timer_new, container, false)
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
                alarmTimerViewModel.setInputsToDefault()
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Get the number pickers for the trigger time
         */
        view.findViewById<ConstraintLayout>(R.id.oneOffTimerNumber).run {
            triggerHourNumberPicker = findViewById(R.id.hourNumber)
            triggerMinuteNumberPicker = findViewById(R.id.minuteNumber)
            triggerSecondNumberPicker = findViewById(R.id.secondNumber)
        }

        /**
         * Get the number pickers for the interval time
         */
        view.findViewById<ConstraintLayout>(R.id.repeatTimerNumber).run {
            repeatHourNumberPicker = findViewById(R.id.hourNumber)
            repeatMinuteNumberPicker = findViewById(R.id.minuteNumber)
            repeatSecondNumberPicker = findViewById(R.id.secondNumber)
        }

        // Set the number picker amount and when the user changes the value update the view model
        triggerHourNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerHour = UnitsOfTime.Hour(progress.toLong())
            }
        }

        // Set the number picker amount and when the user changes the value update the view model
        triggerMinuteNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerMinute = UnitsOfTime.Minute(progress.toLong())
            }
        }

        // Set the number picker amount and when the user changes the value update the view model
        triggerSecondNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerSecond = UnitsOfTime.Second(progress.toLong())
            }
        }

        // Set the number picker amount and when the user changes the value update the view model
        repeatHourNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatHour = UnitsOfTime.Hour(progress.toLong())
            }
        }

        // Set the number picker amount and when the user changes the value update the view model
        repeatMinuteNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatMinute = UnitsOfTime.Minute(progress.toLong())
            }
        }

        // Set the number picker amount and when the user changes the value update the view model
        repeatSecondNumberPicker.run {
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatSecond = UnitsOfTime.Second(progress.toLong())
            }
        }

        saveButton = view.findViewById(R.id.add_alarmtimer)
        alarmTimerTitle = view.findViewById(R.id.timerTitle)
        alarmTypeRadioGroup = view.findViewById(R.id.timerTypeRadioGroup)
        timerStartGroup = view.findViewById(R.id.timerStartGroup)

        immediateStartRadioButton = view.findViewById(R.id.immediateStart)
        delayedStartRadioButton = view.findViewById(R.id.delayedStart)

        /**
         * If a child timer is being created, set the radio text to the two child timer options.
         */
        if (getParentId() != null) {
            immediateStartRadioButton.text = "Starts when parent starts"
            delayedStartRadioButton.text = "Starts when parent ends"
            alarmTimerViewModel.updateTimerStart(TimerStart.ParentStart)
        }

        /**
         * Set the alarm timer type in the view model whenever the user switches types.
         */
        alarmTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            val alarmTimerType: AlarmTimerType =
                if (i == R.id.oneOffRadioButton) AlarmTimerType.OneOffTimer else AlarmTimerType.RepeatingTimer
            alarmTimerViewModel.updateAlarmTimerType(alarmTimerType)
        }

        /**
         * Set the timer start in the view model whenever the user switches start type.
         */
        timerStartGroup.setOnCheckedChangeListener { _, i ->
            val timerStart = if (getParentId() != null) {
                if (i == R.id.immediateStart) TimerStart.ParentStart else TimerStart.ParentEnd
            } else {
                if (i == R.id.immediateStart) TimerStart.Immediate else TimerStart.Delayed
            }

            alarmTimerViewModel.updateTimerStart(timerStart)
        }

        saveButton.setOnClickListener { saveButton ->
            /**
             * If the user didn't change the title, a generic title is generated.
             */
            alarmTimerViewModel.alarmTimerTitle =
                if (alarmTimerTitle.text.toString() == resources.getString(R.string.alarmtimer_title_hint)) {
                    alarmTimerViewModel.createGenericTitle()
                } else {
                    alarmTimerTitle.text.toString()
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
                    alarmTimerViewModel.setInputsToDefault()

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
