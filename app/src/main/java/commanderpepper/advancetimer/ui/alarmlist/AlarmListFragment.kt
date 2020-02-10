package commanderpepper.advancetimer.ui.alarmlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer
import timber.log.Timber


class AlarmListFragment : Fragment() {

    private val viewModel: AlarmListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val list = viewModel.parentAlarmTimers
        Timber.d(list.toString())
        val view = inflater.inflate(R.layout.fragment_alarm_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
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