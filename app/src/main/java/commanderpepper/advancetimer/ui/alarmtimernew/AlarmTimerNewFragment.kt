package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import commanderpepper.advancetimer.R
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
import timber.log.Timber

const val PARENT_KEY = "parentId"

class AlarmTimerNewFragment : Fragment() {

    private lateinit var saveButton: Button
    private val alarmTimerViewModel: AlarmTimerNewViewModel by activityViewModels()

    private lateinit var alarmTimerTitle: EditText

    private lateinit var alarmTypeRadioGroup: RadioGroup

    private lateinit var triggerHourNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var triggerMinuteNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var triggerSecondNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker

    private lateinit var repeatHourNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var repeatMinuteNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker
    private lateinit var repeatSecondNumberPicker: it.sephiroth.android.library.numberpicker.NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_timer_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.oneOffTimerNumber).run {
            triggerHourNumberPicker = findViewById(R.id.hourNumber)
            triggerMinuteNumberPicker = findViewById(R.id.minuteNumber)
            triggerSecondNumberPicker = findViewById(R.id.secondNumber)
        }

        view.findViewById<ConstraintLayout>(R.id.repeatTimerNumber).run {
            repeatHourNumberPicker = findViewById(R.id.hourNumber)
            repeatMinuteNumberPicker = findViewById(R.id.minuteNumber)
            repeatSecondNumberPicker = findViewById(R.id.secondNumber)
        }

        triggerHourNumberPicker.run {
            progress = alarmTimerViewModel.triggerHour.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerHour = UnitsOfTime.Hour(progress.toLong())
            }
        }

        triggerMinuteNumberPicker.run {
            progress = alarmTimerViewModel.triggerMinute.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerMinute = UnitsOfTime.Minute(progress.toLong())
            }
        }

        triggerSecondNumberPicker.run {
            progress = alarmTimerViewModel.triggerSecond.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.triggerSecond = UnitsOfTime.Second(progress.toLong())
            }
        }

        repeatHourNumberPicker.run {
            progress = alarmTimerViewModel.repeatHour.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatHour = UnitsOfTime.Hour(progress.toLong())
            }
        }

        repeatMinuteNumberPicker.run {
            progress = alarmTimerViewModel.repeatMinute.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatMinute = UnitsOfTime.Minute(progress.toLong())
            }
        }

        repeatSecondNumberPicker.run {
            progress = alarmTimerViewModel.repeatSecond.amount.toInt()
            doOnProgressChanged { numberPicker, progress, formUser ->
                alarmTimerViewModel.repeatSecond = UnitsOfTime.Second(progress.toLong())
            }
        }

        saveButton = view.findViewById(R.id.add_alarmtimer)
        alarmTimerTitle = view.findViewById(R.id.timerTitle)
        alarmTypeRadioGroup = view.findViewById(R.id.timerTypeRadioGroup)

        alarmTypeRadioGroup.setOnCheckedChangeListener { _, i ->
            val alarmTimerType: AlarmTimerType =
                if (i == R.id.oneOffRadioButton) AlarmTimerType.OneOffTimer else AlarmTimerType.RepeatingTimer
            alarmTimerViewModel.updateAlarmTimerType(alarmTimerType)
        }

        saveButton.setOnClickListener { saveButton ->

            alarmTimerViewModel.alarmTimerTitle =
                if (alarmTimerTitle.text.toString() == resources.getString(R.string.alarmtimer_title_hint)) {
                    alarmTimerViewModel.createGenericTitle()
                } else {
                    alarmTimerTitle.text.toString()
                }

            val alarmContext = requireContext().applicationContext

            lifecycleScope.launch {
                val resultFlow = withContext(Dispatchers.Default) {
                    alarmTimerViewModel.createTimer(
                        alarmContext, getParentId()
                    )
                }

                resultFlow.flowOn(Dispatchers.Main).collect { _ ->
                    requireActivity().dismissKeyboard()
                    if (getParentId() == null) {
                        view.findNavController()
                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
                    } else {
                        val bundle = bundleOf(DETAIL_TIMER_KEY to getParentId()!!)
                        view.findNavController().navigate(
                            R.id.action_alarmTimerListFragment_to_alarmTimerDetail,
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
