package commanderpepper.advancetimer.ui.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.getTypeAsString
import io.reactivex.BackpressureOverflowStrategy
import java.util.*

class AlarmTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var title: TextView
    private lateinit var enabled: TextView
    private lateinit var timerTime: TextView
    private lateinit var type: TextView
    private lateinit var timerStrategy: TextView

    fun bind(
        alarmTimer: AlarmTimer,
        onClick: (View) -> Unit
    ) {
        view.setOnClickListener(onClick)

        title = view.findViewById(R.id.parent_alarmtimer_itemview_name)
        enabled = view.findViewById(R.id.isEnabled)
        type = view.findViewById(R.id.alarmType)
        timerTime = view.findViewById(R.id.timerTime)
        timerStrategy = view.findViewById(R.id.timerStrategy)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = alarmTimer.triggerTime.amount
        }

        title.text = "Title: ${alarmTimer.title}"
        enabled.text = "Status: ${if (alarmTimer.enabled) "On" else "Off"}"
        type.text = "Type: ${alarmTimer.type.getTypeAsString()}"
        timerTime.text = "Trigger time: ${calendar.time}"

        if (alarmTimer.parentID != null) {
            timerStrategy.visibility = View.VISIBLE
            timerStrategy.text = when (alarmTimer.timerStart) {
                TimerStart.ParentStart -> "Strategy: Parent Starts"
                else -> "Strategy: Parent Ends"
            }
        }
    }

}