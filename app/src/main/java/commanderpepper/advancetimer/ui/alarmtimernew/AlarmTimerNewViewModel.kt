package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import commanderpepper.App
import commanderpepper.advancetimer.model.*
import commanderpepper.advancetimer.room.AlarmTimerType
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class AlarmTimerNewViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmTimerViewModel: AlarmTimerViewModel =
        (application as App).appComponent.alarmTimerViewModelGenerator()

    var triggerHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var triggerMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var triggerSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var alarmTimerType: AlarmTimerType = AlarmTimerType.OneOffTimer
        private set(value){
            _timerSetsOffText.value = if(value is AlarmTimerType.OneOffTimer) "Timer length:" else "Initial Delay Length:"
            field = value
        }

    var alarmTimerTitle = "Timer Title"

    var timerStart: TimerStart = TimerStart.Immediate
        private set

    private val _timerSetsOffText : MutableLiveData<String> = MutableLiveData("Timer length:")
    val timerSetsOffText : LiveData<String> = _timerSetsOffText

    fun updateAlarmTimerType(alarmTimerType: AlarmTimerType) {
        this.alarmTimerType = alarmTimerType
    }

    fun updateTimerStart(timerStart: TimerStart) {
        this.timerStart = timerStart
        Timber.d("$timerStart")
    }

    fun onTimerTitleChange(s: CharSequence, start: Int, before: Int, count: Int) {
        alarmTimerTitle = s.toString()
    }

    /**
     * The UI view model will ask the generic view model to create a timer. The generic view timer will return a Flow.
     */
    suspend fun createTimer(
        parentId: Int?
    ): Flow<Int> {

        val timeInMilliSecond = calculateTimeInMilliseconds(triggerHour, triggerMinute, triggerSecond)

        return alarmTimerViewModel.createTimer(
            alarmTimerTitle,
            parentId,
            alarmTimerType,
            timeInMilliSecond,
            timerStart
        )
    }

    fun createGenericTitle(): String {
        return "${triggerHour.amount}h:${triggerMinute.amount}m:${triggerSecond.amount}s Timer"
    }


}