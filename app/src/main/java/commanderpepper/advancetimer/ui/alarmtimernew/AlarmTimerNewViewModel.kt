package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import commanderpepper.App
import commanderpepper.advancetimer.model.UnitsOfTime
import commanderpepper.advancetimer.model.plus
import commanderpepper.advancetimer.model.toMillisecond
import commanderpepper.advancetimer.room.AlarmTimerType
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.flow.Flow
import java.util.*

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

    fun updateAlarmTimerType(alarmTimerType: AlarmTimerType) {
        this.alarmTimerType = alarmTimerType
    }

    /**
     * The UI view model will ask the generic view model to create a timer. The generic view timer will return a Flow.
     */
    suspend fun createTimer(
        context: Context,
        parentId: Int?
    ): Flow<Int> {
        return alarmTimerViewModel.createTimer(
            alarmTimerTitle,
            context,
            getTriggerTime(calculateTimeInMilliseconds(triggerHour, triggerMinute, triggerSecond)),
            parentId,
            alarmTimerType,
            calculateTimeInMilliseconds(repeatHour, repeatMinute, repeatSecond)
        )
    }

    fun createGenericTitle(): String {
        return "Timer goes off in ${triggerHour.amount}h:${triggerMinute.amount}m:${triggerSecond.amount}s"
    }

    /**
     * The trigger time will be the time of alarm creation plus whatever the user inputted.
     */
    private fun getTriggerTime(triggerTime: UnitsOfTime.MilliSecond): UnitsOfTime.MilliSecond {
        val calendar = Calendar.getInstance()
        return UnitsOfTime.MilliSecond(calendar.timeInMillis) + triggerTime
    }

    private fun calculateTimeInMilliseconds(
        hour: UnitsOfTime.Hour,
        minute: UnitsOfTime.Minute,
        second: UnitsOfTime.Second
    ): UnitsOfTime.MilliSecond {
        return hour.toMillisecond() + minute.toMillisecond() + second.toMillisecond()
    }

}

