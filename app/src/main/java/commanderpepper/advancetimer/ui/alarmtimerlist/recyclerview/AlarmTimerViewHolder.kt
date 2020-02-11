package commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.room.AlarmTimer
import timber.log.Timber

class AlarmTimerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(alarmTimer: AlarmTimer) {
        Timber.d("Do something here")
    }
}