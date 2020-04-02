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

    var triggerHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var triggerMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var triggerSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var repeatHour: UnitsOfTime.Hour = UnitsOfTime.Hour(0L)
    var repeatMinute: UnitsOfTime.Minute = UnitsOfTime.Minute(0L)
    var repeatSecond: UnitsOfTime.Second = UnitsOfTime.Second(0L)

    var alarmTimerType: AlarmTimerType = AlarmTimerType.OneOffTimer
        private set

    fun updateAlarmTimerType(alarmTimerType: AlarmTimerType) {
        this.alarmTimerType = alarmTimerType
    }

    var alarmTimerTitle = "Timer Title"

    private val alarmTimerViewModel: AlarmTimerViewModel =
        (application as App).appComponent.alarmTimerViewModelGenerator()

//    fun makeTimerUsingContext(
//        title: String,
//        context: Context,
//        triggerAtMillis: Long,
//        parentId: Int?
//    ) {
//        alarmTimerViewModel.createTimerWaitForId(title, context, triggerAtMillis, parentId)
//    }

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
        return "Timer goes off in ${triggerHour}h:${triggerMinute}m:${triggerSecond}s"
    }

    fun getTriggerTime(triggerTime: UnitsOfTime.MilliSecond): UnitsOfTime.MilliSecond {
        val calendar = Calendar.getInstance()
        return UnitsOfTime.MilliSecond(calendar.timeInMillis) + triggerTime
    }

    fun calculateTimeInMilliseconds(
        hour: UnitsOfTime.Hour,
        minute: UnitsOfTime.Minute,
        second: UnitsOfTime.Second
    ): UnitsOfTime.MilliSecond {
        return hour.toMillisecond() + minute.toMillisecond() + second.toMillisecond()
    }

//    suspend fun createTimer(
//        title: String,
//        context: Context,
//        triggerAtMillis: Long,
//        parentId: Int?
//    ): Flow<Int> {
//        return alarmTimerViewModel.createTimer(title, context, triggerAtMillis, parentId)
//    }
}

