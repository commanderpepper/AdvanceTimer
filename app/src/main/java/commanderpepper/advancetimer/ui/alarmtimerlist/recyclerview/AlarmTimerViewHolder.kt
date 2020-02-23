package commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.getTypeAsString
import java.util.*

class AlarmTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var title: TextView
    private lateinit var enabled: TextView
    private lateinit var timeLeft: TextView
    private lateinit var type: TextView

    fun bind(alarmTimer: AlarmTimer) {
        title = view.findViewById(R.id.parent_alarmtimer_itemview_name)
        enabled = view.findViewById(R.id.isEnabled)
        timeLeft = view.findViewById(R.id.timeleft)
        type = view.findViewById(R.id.alarmType)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = alarmTimer.timeInMillis
        }

        title.text = alarmTimer.title
        enabled.text = if (alarmTimer.enabled) "On" else "Off"
        timeLeft.text = calendar.time.toString()
        type.text = alarmTimer.type.getTypeAsString()
    }
}