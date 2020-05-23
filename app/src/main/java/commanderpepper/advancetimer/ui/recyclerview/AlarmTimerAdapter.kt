package commanderpepper.advancetimer.ui.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.ui.NavGraphAction


class AlarmTimerAdapter(val list: List<AlarmTimer>, val navGraphAction: NavGraphAction) :
    RecyclerView.Adapter<AlarmTimerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmTimerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.parent_alarmtimer_itemview, parent, false)
        return AlarmTimerViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AlarmTimerViewHolder, position: Int) {
        holder.bind(list[position]) { view ->
            if (navGraphAction.action != 0) {
                val bundle = bundleOf("alarmTimerId" to list[position].id)
                val navController = view.findNavController()
                navController.navigate(navGraphAction.action, bundle)
            }
        }
    }
}

