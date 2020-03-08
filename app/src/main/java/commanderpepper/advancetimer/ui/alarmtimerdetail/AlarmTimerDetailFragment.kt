package commanderpepper.advancetimer.ui.alarmtimerdetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import commanderpepper.advancetimer.ui.alarmtimernew.PARENT_KEY
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

val DETAIL_TIMER_KEY = "alarmTimerId"
val FAB_KEY = "showAddTimerFAB"

class AlarmTimerDetailFragment : Fragment() {

    private val viewModel: AlarmTimerDetailViewModel by activityViewModels()
    private lateinit var detailTimerTitleView: TextView
    private lateinit var parentDetailTimerTitleView: TextView
    private lateinit var childTimersRecyclerView: RecyclerView
    private lateinit var addTimerFab: FloatingActionButton

    private val alarmTimerId: Int = getAlarmTimerId()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Timber.d("ID is $alarmTimerId")

        return inflater.inflate(R.layout.fragment_alarm_timer_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTimerTitleView = view.findViewById(R.id.detailTimerTitle)
        parentDetailTimerTitleView = view.findViewById(R.id.detailParentTimerTitle)
        childTimersRecyclerView = view.findViewById(R.id.detailChildTimerList)
        addTimerFab = view.findViewById(R.id.detail_create_alarmtimer_fab)

        if (getAddFabStatus()) {
            addTimerFab.setOnClickListener {
                Timber.d("It workds")

                val bundle = bundleOf(PARENT_KEY to getAlarmTimerId())

                view.findNavController()
                    .navigate(R.id.action_alarmTimerDetail_to_alarmTimerNew, bundle)
            }
        } else {
            addTimerFab.hide()
        }

        lifecycleScope.launch {
            val title = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveTimer(getAlarmTimerId()).title
            }
            detailTimerTitleView.text = "Title: $title"
        }

        lifecycleScope.launch {
            parentDetailTimerTitleView.text = withContext(lifecycleScope.coroutineContext) {
                viewModel.retrieveParentTimerTitle(getAlarmTimerId())
            }
        }

        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(getAlarmTimerId())

            val adapter =
                AlarmTimerAdapter(
                    childTimers,
                    if (getAddFabStatus()) {
                        NavGraphAction(R.id.action_alarmTimerDetail_self)
                    } else {
                        NavGraphAction(
                            0
                        )
                    }
                )
            val manager = LinearLayoutManager(this@AlarmTimerDetailFragment.context)

            val dividerItemDecoration =
                DividerItemDecoration(this@AlarmTimerDetailFragment.context, manager.orientation)

            childTimersRecyclerView.adapter = adapter
            childTimersRecyclerView.layoutManager = manager
            childTimersRecyclerView.addItemDecoration(dividerItemDecoration)

        }
    }

    private fun getAlarmTimerId(): Int {
        return arguments?.getInt(DETAIL_TIMER_KEY) ?: -1
    }

    private fun getAddFabStatus(): Boolean {
        return arguments?.getBoolean(FAB_KEY, true) ?: true
    }

}
