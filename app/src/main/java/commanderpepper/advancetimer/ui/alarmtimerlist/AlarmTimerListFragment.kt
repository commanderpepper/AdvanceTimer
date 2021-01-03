package commanderpepper.advancetimer.ui.alarmtimerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import commanderpepper.advancetimer.DataBinderMapperImpl
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.databinding.FragmentAlarmTimerListBinding
import commanderpepper.advancetimer.databinding.FragmentAlarmTimerNewBinding
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.android.synthetic.main.fragment_alarm_timer_list.view.*
import kotlinx.coroutines.launch


class AlarmTimerListFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    private lateinit var binding : FragmentAlarmTimerListBinding

    private val viewModel: AlarmListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_timer_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.root.alarmtimer_recyclerview
        fab = binding.root.create_alarmtimer_fab

        fab.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_alarmTimerListFragment_to_alarmTimerNew)
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val list = viewModel.getParentAlarmTimerList()

            val adapter =
                AlarmTimerAdapter(
                    list,
                    NavGraphAction(R.id.action_alarmTimerListFragment_to_alarmTimerDetail)
                )

            val manager = LinearLayoutManager(context)

            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                manager.orientation
            )

            recyclerView.adapter = adapter
            recyclerView.layoutManager = manager
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

}


