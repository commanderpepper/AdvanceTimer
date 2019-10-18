package commanderpepper.advancetimer.ui.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService

import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.receivers.AlarmReceiver

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AlarmList.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AlarmList.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmList : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button: Button = getView()!!.findViewById(R.id.startAlarm)
        button.setOnClickListener {
            // Get AlarmManager instance
            val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Intent part
            val intent = Intent(activity, AlarmReceiver::class.java)
            intent.action = "BASIC"
            intent.putExtra("MESSAGE", "Medium AlarmManager Demo")

            val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)

            // Alarm time
            val ALARM_DELAY_IN_SECOND = 2
            val alarmTimeAtUTC = System.currentTimeMillis() + ALARM_DELAY_IN_SECOND * 1_000L

            // Set with system Alarm Service
            // Other possible functions: setExact() / setRepeating() / setWindow(), etc
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeAtUTC,
                pendingIntent
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlarmList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlarmList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
