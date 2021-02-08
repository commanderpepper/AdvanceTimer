package commanderpepper.advancetimer.ui.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.databinding.ParentAlarmtimerItemviewBinding
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.getTypeAsString
import io.reactivex.BackpressureOverflowStrategy
import kotlinx.android.synthetic.main.fragment_alarm_timer_new.view.*
import java.util.*

class AlarmTimerViewHolder(private val binding: ParentAlarmtimerItemviewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        alarmTimer: AlarmTimer,
        onClick: (View) -> Unit
    ) {
        binding.root.setOnClickListener(onClick)
        binding.alarmTimer = alarmTimer

        val calendar = Calendar.getInstance().apply {
            timeInMillis = alarmTimer.calendarTime.amount
        }

        binding.isEnabled.text = "Status: ${if (alarmTimer.enabled) "On" else "Off"}"
        binding.alarmType.text = "Type: ${alarmTimer.type.getTypeAsString()}"
        binding.timerTime.text = "Trigger time: ${calendar.time}"

        if (alarmTimer.parentID != null) {
            binding.timerStrategy.visibility = View.VISIBLE
            binding.timerStrategy.text = when (alarmTimer.timerStart) {
                TimerStart.ParentStart -> "Strategy: Parent Starts"
                else -> "Strategy: Parent Ends"
            }
        }
    }

}