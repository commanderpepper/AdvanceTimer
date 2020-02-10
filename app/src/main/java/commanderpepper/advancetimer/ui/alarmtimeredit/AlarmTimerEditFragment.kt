package commanderpepper.advancetimer.ui.alarmtimeredit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import commanderpepper.advancetimer.R

class AlarmTimerEditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarmtimer_edit, container, false)
        return view
    }
}