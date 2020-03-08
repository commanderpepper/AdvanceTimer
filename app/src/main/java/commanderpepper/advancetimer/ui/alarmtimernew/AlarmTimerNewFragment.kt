package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.NavGraphAction
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_timer_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveButton = view.findViewById(R.id.add_alarmtimer)

        hourEditText = view.findViewById(R.id.newHour)
        minuteEditText = view.findViewById(R.id.newMinute)
        secondEditText = view.findViewById(R.id.newSecond)

        alarmTimerTitle = view.findViewById(R.id.timerTitle)

        saveButton.setOnClickListener {

            val hourAsString = hourEditText.text.toString()
            val minuteAsString = minuteEditText.text.toString()
            val secondAsString = secondEditText.text.toString()

            Timber.d(hourAsString)
            Timber.d(minuteAsString)
            Timber.d(secondAsString)

            val hourAsLong = hourToMilliseconds(hourAsString)
            val minuteAsLong = minuteToMilliseconds(minuteAsString)
            val secondAsLong = secondToMilliseconds(secondAsString)

            Timber.d(hourAsLong.toString())
            Timber.d(minuteAsLong.toString())
            Timber.d(secondAsLong.toString())

            val title =
                if (alarmTimerTitle.text.toString() == resources.getString(R.string.alarmtimer_title_hint)) {
                    "${hourAsString}h:${minuteAsString}m:${secondAsString}s"
                } else {
                    alarmTimerTitle.text.toString()
                }

            val alarmContext = requireContext().applicationContext

            val nowMilliSeconds: Long = Calendar.getInstance().timeInMillis

            val triggerAtMillis = nowMilliSeconds + hourAsLong + minuteAsLong + secondAsLong

            lifecycleScope.launch {
                val resultFlow = withContext(Dispatchers.Default) {
                    alarmTimerViewModel.createTimer(
                        title, alarmContext, triggerAtMillis, getParentId()
                    )
                }

                resultFlow.flowOn(Dispatchers.Main).collect { _ ->
                    requireActivity().dismissKeyboard()
                    if (getParentId() == null) {
                        it.findNavController()
                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
                    } else {
                        val bundle = bundleOf(DETAIL_TIMER_KEY to getParentId()!!)
                        it.findNavController()
                            .navigate(R.id.action_alarmTimerNew_to_alarmTimerDetail, bundle)
//                        this@AlarmTimerNewFragment.parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    private fun getParentId(): Int? {
        return arguments?.getInt(PARENT_KEY)
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
