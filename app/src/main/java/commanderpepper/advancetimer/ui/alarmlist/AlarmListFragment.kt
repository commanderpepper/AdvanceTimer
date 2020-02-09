package commanderpepper.advancetimer.ui.alarmlist

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.receivers.AlarmReceiver
import commanderpepper.advancetimer.room.AlarmTimer
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AlarmListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm_list, container, false)
        return view
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
            AlarmListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class AlarmTimerAdapter(val list: List<AlarmTimer>) : RecyclerView.Adapter<AlarmTimerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmTimerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.parent_alarmtimer_itemview, parent, false)
        return AlarmTimerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlarmTimerViewHolder, position: Int) {
        holder.bind(list[position])
    }

}

class AlarmTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(alarmTimer: AlarmTimer) {
        Timber.d("Do something here")
    }
}