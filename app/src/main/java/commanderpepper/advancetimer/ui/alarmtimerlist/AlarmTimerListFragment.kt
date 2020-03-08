package commanderpepper.advancetimer.ui.alarmtimerlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch
import timber.log.Timber


class AlarmTimerListFragment : Fragment() {

    //    private val navController = findNavController()
    private lateinit var fab: FloatingActionButton
    private val viewModel: AlarmListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Timber.d(this.parentFragmentManager.backStackEntryCount.toString())
        return inflater.inflate(R.layout.fragment_alarm_timer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val list = viewModel.getParentAlarmTimerList()
//            Timber.d(list.toString())
            val adapter =
                AlarmTimerAdapter(
                    list,
                    NavGraphAction(R.id.action_alarmTimerListFragment_to_alarmTimerDetail)
                )
            val recyclerView: RecyclerView = view.findViewById(R.id.alarmtimer_recyclerview)
            val manager = LinearLayoutManager(context)

            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                manager.orientation
            )
            recyclerView.adapter = adapter
            recyclerView.layoutManager = manager
            recyclerView.addItemDecoration(dividerItemDecoration)
        }

        fab = view.findViewById(R.id.create_alarmtimer_fab)

        fab.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_alarmTimerListFragment_to_alarmTimerNew)
        }
    }

}


