package commanderpepper.advancetimer.ui.alarmtimerlist

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.alarmtimerlist.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class AlarmTimerListFragment : Fragment() {

    //    private val navController = findNavController()
    private lateinit var fab: FloatingActionButton
    private val viewModel: AlarmListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_timer_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val list = viewModel.getParentAlarmTimerList()
            val adapter = AlarmTimerAdapter(list)
            val recyclerView: RecyclerView = view.findViewById(R.id.alarmtimer_recyclerview)
            val manager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = manager
        }

        fab = view.findViewById(R.id.create_alarmtimer_fab)

        fab.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_alarmTimerListFragment_to_alarmTimerNew)
        }
    }

}


