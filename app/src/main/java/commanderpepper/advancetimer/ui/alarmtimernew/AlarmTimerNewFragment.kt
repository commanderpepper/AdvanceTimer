package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimerType
import commanderpepper.advancetimer.ui.alarmtimerdetail.DETAIL_TIMER_KEY
import commanderpepper.advancetimer.viewmodel.dismissKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

const val PARENT_KEY = "parentId"

class AlarmTimerNewFragment : Fragment() {

    private lateinit var saveButton: Button
    private val alarmTimerViewModel: AlarmTimerNewViewModel by activityViewModels()

    private lateinit var hourEditText: EditText
    private lateinit var minuteEditText: EditText
    private lateinit var secondEditText: EditText

    private lateinit var alarmTimerTitle: EditText

    private lateinit var alarmTypeRadioGroup: RadioGroup

    private lateinit var triggerHourNumberPicker: NumberPicker
    private lateinit var triggerMinuteNumberPicker: NumberPicker
    private lateinit var triggerSecondNumberPicker: NumberPicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarm_timer_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ConstraintLayout>(R.id.oneOffTimerNumber).also {
            triggerHourNumberPicker = it.findViewById(R.id.hourNumber)
            triggerMinuteNumberPicker = it.findViewById(R.id.minuteNumber)
            triggerSecondNumberPicker = it.findViewById(R.id.secondNumber)
        }

        triggerHourNumberPicker.run {
            setMinValueForTime()
            setMaxValueForTime()
            setPickerValue(alarmTimerViewModel.triggerHour)
            setOnValueChangedListener { _, _, newVal ->
                alarmTimerViewModel.triggerHour = newVal
            }
        }

        triggerMinuteNumberPicker.run {
            setMaxValueForTime()
            setMinValueForTime()
            setPickerValue(alarmTimerViewModel.triggerMinute)
            setOnValueChangedListener { _, _, newVal ->
                alarmTimerViewModel.triggerMinute = newVal
            }
        }

        triggerSecondNumberPicker.run {
            setMaxValueForTime()
            setMinValueForTime()
            setPickerValue(alarmTimerViewModel.triggerSecond)
            setOnValueChangedListener { _, _, newVal ->
                alarmTimerViewModel.triggerSecond = newVal
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

//        saveButton.setOnClickListener {
//
//            val hourAsString = hourEditText.text.toString()
//            val minuteAsString = minuteEditText.text.toString()
//            val secondAsString = secondEditText.text.toString()
//
//            val hourAsLong = hourToMilliseconds(hourAsString)
//            val minuteAsLong = minuteToMilliseconds(minuteAsString)
//            val secondAsLong = secondToMilliseconds(secondAsString)
//
//            val title =
//                if (alarmTimerTitle.text.toString() == resources.getString(R.string.alarmtimer_title_hint)) {
//                    createGenericTitle(hourAsString, minuteAsString, secondAsString)
//                } else {
//                    alarmTimerTitle.text.toString()
//                }
//
//            val alarmContext = requireContext().applicationContext
//
//            val nowMilliSeconds: Long = Calendar.getInstance().timeInMillis
//
//            val triggerAtMillis = nowMilliSeconds + hourAsLong + minuteAsLong + secondAsLong
//
//            lifecycleScope.launch {
//                val resultFlow = withContext(Dispatchers.Default) {
//                    alarmTimerViewModel.createTimer(
//                        title, alarmContext, triggerAtMillis, getParentId()
//                    )
//                }
//
//                resultFlow.flowOn(Dispatchers.Main).collect { _ ->
//                    requireActivity().dismissKeyboard()
//                    if (getParentId() == null) {
//                        it.findNavController()
//                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
//                    } else {
//                        val bundle = bundleOf(DETAIL_TIMER_KEY to getParentId()!!)
//                        it.findNavController()
//                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerDetail, bundle)
//                    }
//                }
//            }
//        }
    }

    private fun NumberPicker.setMaxValueForTime(maxValue: Int = 99) {
        this.maxValue = maxValue
    }

    private fun NumberPicker.setMinValueForTime(minValue: Int = 0) {
        this.minValue = minValue
    }

    private fun NumberPicker.setPickerValue(value: Int) {
        this.value = value
    }


    private fun getParentId(): Int? {
        return arguments?.getInt(PARENT_KEY)
    }

    override fun onPause() {
        super.onPause()
        Timber.d(alarmTimerViewModel.alarmTimerType.toString())
    }

    private fun createGenericTitle(hour: String, minute: String, second: String): String {
        return "${hour}h:${minute}m:${second}s"
    }
}


fun hourToMilliseconds(hour: String): Long {
    return hour.toLong() * 3_600_000L
}

fun minuteToMilliseconds(minute: String): Long {
    return minute.toLong() * 60_000L
}

fun secondToMilliseconds(second: String): Long {
    return second.toLong() * 1_000L
}
