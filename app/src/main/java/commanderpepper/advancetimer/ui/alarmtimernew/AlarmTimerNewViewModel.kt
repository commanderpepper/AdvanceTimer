package commanderpepper.advancetimer.ui.alarmtimernew

import androidx.lifecycle.ViewModel
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel

class AlarmTimerNewViewModel : ViewModel(){
    private val alarmTimerViewModel = AlarmTimerViewModel.get()

    fun saveAlarm(){
        alarmTimerViewModel
    }
}