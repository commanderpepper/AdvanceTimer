package commanderpepper.advancetimer.ui.recyclerview

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

    fun bind(
        alarmTimer: AlarmTimer,
        onClick: (View) -> Unit
    ) {
        view.setOnClickListener(onClick)

        title = view.findViewById(R.id.parent_alarmtimer_itemview_name)
        enabled = view.findViewById(R.id.isEnabled)
        type = view.findViewById(R.id.alarmType)
        timeLeft = view.findViewById(R.id.timeleft)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = alarmTimer.triggerTime.amount
        }

        title.text = "Title: ${alarmTimer.title}"
        enabled.text = "Status: ${if (alarmTimer.enabled) "On" else "Off"}"
        type.text = "Type ${alarmTimer.type.getTypeAsString()}"
        timeLeft.text = "Time: ${calendar.time}"
    }

}