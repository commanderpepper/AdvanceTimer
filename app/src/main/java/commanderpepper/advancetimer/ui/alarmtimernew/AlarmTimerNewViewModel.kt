package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    var repeatHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var repeatMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var repeatSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var alarmTimerType: AlarmTimerType = AlarmTimerType.OneOffTimer
        private set

    var alarmTimerTitle = "Timer Title"

    var timerStart: TimerStart = TimerStart.Immediate
        private set

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
        return alarmTimerViewModel.createTimer(
            alarmTimerTitle,
            getTriggerTime(calculateTimeInMilliseconds(triggerHour, triggerMinute, triggerSecond)),
            parentId,
            alarmTimerType,
            calculateTimeInMilliseconds(repeatHour, repeatMinute, repeatSecond),
            calculateTimeInMilliseconds(triggerHour, triggerMinute, triggerSecond),
            timerStart
        )
    }

    fun createGenericTitle(): String {
        return "Timer goes off in ${triggerHour.amount}h:${triggerMinute.amount}m:${triggerSecond.amount}s"
    }

    private fun calculateTimeInMilliseconds(
        hour: UnitsOfTime.Hour,
        minute: UnitsOfTime.Minute,
        second: UnitsOfTime.Second
    ): UnitsOfTime.MilliSecond {
        return hour.toMillisecond() + minute.toMillisecond() + second.toMillisecond()
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