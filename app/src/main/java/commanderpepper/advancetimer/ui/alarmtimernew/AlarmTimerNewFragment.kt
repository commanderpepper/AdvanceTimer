package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.receivers.MyReceiver
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.launch
import java.util.*


class AlarmTimerNewFragment : Fragment() {

    private lateinit var saveButton: Button
    private val alarmTimerViewModel: AlarmTimerNewViewModel by activityViewModels()

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

        saveButton.setOnClickListener {

            val alarmContext = context!!.applicationContext

            val nowMilliSeconds: Long = Calendar.getInstance().timeInMillis

            val thirtySecondsFromNow = nowMilliSeconds + 30000L

            alarmTimerViewModel.makeTimerUsingContext(alarmContext, thirtySecondsFromNow)

//            val requestCodeGenerator = RequestCodeGenerator.get()
//
//            val alarmManager: AlarmManager =
//                context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//            val intent = Intent(context!!, MyReceiver::class.java)
//
//            val pendingIntent: PendingIntent =
//                PendingIntent.getBroadcast(
//                    context!!,
//                    requestCodeGenerator.getRequestCode(),
//                    intent,
//                    PendingIntent.FLAG_CANCEL_CURRENT
//                )
//
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, thirtySecondsFromNow, pendingIntent)

            it.findNavController()
                .navigate(R.id.action_alarmTimerNew_to_alarmTimerListFragment)
        }
    }


}
