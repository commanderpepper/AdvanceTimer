package commanderpepper.advancetimer.ui.alarmtimeredit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import commanderpepper.advancetimer.R

class AlarmTimerEditFragment : Fragment() {

    private lateinit var editButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alarmtimer_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editButton = view.findViewById(R.id.edit_alarmtimer)
    }
}