package commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer
import timber.log.Timber

class AlarmTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var text: TextView

    fun bind(alarmTimer: AlarmTimer) {
        text = view.findViewById(R.id.parent_alarmtimer_itemview_name)
        text.text = alarmTimer.toString()
    }
}