package commanderpepper.advancetimer.ui.dismisstimer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.databinding.FragmentAlarmTimerDismissBinding
import commanderpepper.advancetimer.ui.NavGraphAction
import commanderpepper.advancetimer.ui.alarmtimerdetail.DETAIL_TIMER_KEY
import commanderpepper.advancetimer.ui.recyclerview.AlarmTimerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AlarmTimerDismissFragment : Fragment() {

    private val viewModel: AlarmTimerDismissViewModel by viewModels()

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: FragmentAlarmTimerDismissBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_timer_dismiss, container, false)
        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Set the timer title
         */
        lifecycleScope.launch {
            binding.dismissTimerTitle.text = viewModel.retrieveParentTimerTitle(getAlarmTimerId())
        }

        if(viewModel.mediaPlayerState !is MediaPlayerState.Stopped){
            mediaPlayer = MediaPlayer.create(this.context, R.raw.bell_ringing_04).apply {
                start()
                isLooping = true
            }
            viewModel.setMediaPlayerToPlaying()
        }

        /**
         * Modify the timer
         */
        lifecycleScope.launch {
            viewModel.modifyEnabledState(getAlarmTimerId())
            viewModel.modifyTriggerTime(getAlarmTimerId())
//            viewModel.renableTimer(getAlarmTimerId())
        }

        /**
         * Set the timer title
         */
        lifecycleScope.launch {
            binding.dismissTimerTitle.text = getString(R.string.timerTitleDetail, viewModel.retrieveTimerTitle(getAlarmTimerId()))
        }

        /**
         * Set parent timer title
         */
        lifecycleScope.launch {
            binding.dismissParentTimerTitle.text = getString(R.string.parentTimerTitleDetail, viewModel.retrieveParentTimerTitle(getAlarmTimerId()))
        }

        /**
         * Set up the adapter for the recycler view.
         */
        lifecycleScope.launch {
            val childTimers = viewModel.retrieveChildTimers(getAlarmTimerId())

            val adapter = AlarmTimerAdapter(childTimers, NavGraphAction(0))

            val manager = LinearLayoutManager(this@AlarmTimerDismissFragment.context)

            val dividerItemDecoration =
                DividerItemDecoration(this@AlarmTimerDismissFragment.context, manager.orientation)

            binding.dismissChildTimerList

            binding.dismissChildTimerList.adapter = adapter
            binding.dismissChildTimerList.layoutManager = manager
            binding.dismissChildTimerList.addItemDecoration(dividerItemDecoration)
        }

        binding.dismissTurnOffButton.setOnClickListener {
            lifecycleScope.launch {
                Timber.d("Alarm to disable ${getAlarmTimerId()}")
                viewModel.stopTimer(getAlarmTimerId())
            }
            stopMediaPlayer()
            Toast.makeText(this@AlarmTimerDismissFragment.requireContext(), "Timer turned off", Toast.LENGTH_SHORT).show()
        }

        binding.dismissTimerButton.setOnClickListener {
            stopMediaPlayer()
            Toast.makeText(this@AlarmTimerDismissFragment.requireContext(), "Timer dismissed", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Get alarm timer id passed to this fragment.
     */
    private fun getAlarmTimerId(): Int {
        return arguments?.getInt(DETAIL_TIMER_KEY) ?: -1
    }

    private fun stopMediaPlayer(){
        if(viewModel.mediaPlayerState !is MediaPlayerState.Stopped){
            mediaPlayer.stop()
            viewModel.setMediaPlayerToStopped()
        }
    }

    override fun onPause() {
        super.onPause()
        /**
         * Stop the media player when the fragment is no longer the main focus but do not set the state to stopped.
         * The state is set to stopped when the user dismisses or turns off the alarm.
         */
        if(viewModel.mediaPlayerState  !is MediaPlayerState.Stopped){
            mediaPlayer.stop()
        }
    }
}