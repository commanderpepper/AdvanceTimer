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

    var timeHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var timeMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var timeSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var triggerHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var triggerMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var triggerSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var repeatHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var repeatMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var repeatSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var alarmTimerType: AlarmTimerType = AlarmTimerType.OneOffTimer
        private set

    var alarmTimerTitle = "Timer Title"

    var timerStart: TimerStart = TimerStart.Immediate
        private set

    private val _repeatVisibility = MutableLiveData<Boolean>(alarmTimerType == AlarmTimerType.RepeatingTimer)
    val repeatVisibility : LiveData<Boolean> = _repeatVisibility

    private val _delayVisibility = MutableLiveData<Boolean>(timerStart == TimerStart.Delayed)
    val delayVisibility : LiveData<Boolean> = _delayVisibility

    fun updateAlarmTimerType(alarmTimerType: AlarmTimerType) {
        this.alarmTimerType = alarmTimerType
    }

    fun updateRepeatVisibility(visibility: Boolean){
        _repeatVisibility.value = visibility
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

        val delayTimeInMilliSecond = calculateTimeInMilliseconds(triggerHour, triggerMinute, triggerSecond)
        val repeatTimeInMilliSecond = calculateTimeInMilliseconds(repeatHour, repeatMinute, repeatSecond)


        return alarmTimerViewModel.createTimer(
            alarmTimerTitle,
            repeatTimeInMilliSecond,
            parentId,
            alarmTimerType,
            delayTimeInMilliSecond,
            timerStart
        )
    }

    fun createGenericTitle(): String {
        return "${triggerHour.amount}h:${triggerMinute.amount}m:${triggerSecond.amount}s Timer"
    }

    /**
     * Called when leaving the fragment via the save button
     */
    fun setInputsToDefault() {
        triggerHour = UnitsOfTime.Hour(0L)
        triggerMinute = UnitsOfTime.Minute(0L)
        triggerSecond = UnitsOfTime.Second(0L)

        repeatHour = UnitsOfTime.Hour(0L)
        repeatMinute = UnitsOfTime.Minute(0L)
        repeatSecond = UnitsOfTime.Second(0L)

        alarmTimerType = AlarmTimerType.OneOffTimer
        timerStart = TimerStart.Immediate

        alarmTimerTitle = "Timer Title"
    }


}