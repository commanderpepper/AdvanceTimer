package commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer


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