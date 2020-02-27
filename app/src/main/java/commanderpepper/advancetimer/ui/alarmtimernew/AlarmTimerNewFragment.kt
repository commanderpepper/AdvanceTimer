package commanderpepper.advancetimer.ui.alarmtimernew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.viewmodel.dismissKeyboard
import kotlinx.android.synthetic.main.fragment_alarm_timer_new.*
import timber.log.Timber
import java.util.*


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

            alarmTimerViewModel.makeTimerUsingContext(
                title,
                alarmContext,
                triggerAtMillis
            )

//            val requestCodeGenerator = RequestCodeGenerator.get()
//
//            val alarmManager: AlarmManager =
//                alarmContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//            val intent = Intent(context!!, MyReceiver::class.java)
//
//            val pendingIntent: PendingIntent =
//                PendingIntent.getBroadcast(
//                    context!!,
//                    requestCodeGenerator.getRequestCode(),
//                    intent,
//                    PendingIntent.FLAG_CANCEL_CURRENT,
//                )
//
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, thirtySecondsFromNow, pendingIntent)

            requireActivity().dismissKeyboard()

            it.findNavController()
                .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
        }
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
