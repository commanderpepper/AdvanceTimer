package commanderpepper.advancetimer.model

import androidx.room.TypeConverter

sealed class TimerStart {
    object Immediate : TimerStart()
    object Delayed : TimerStart()
    object ParentStart : TimerStart()
    object ParentEnd : TimerStart()
}

class TimerStartConverter() {

    @TypeConverter
    fun stringToTimerStart(timerStart: String): TimerStart {
        return when (timerStart) {
            "Immediate" -> TimerStart.Immediate
            "Delayed" -> TimerStart.Delayed
            "ParentStart" -> TimerStart.ParentStart
            else -> TimerStart.ParentEnd
        }
    }

    @TypeConverter
    fun timerStartToString(timerStart: TimerStart): String {
        return when (timerStart) {
            TimerStart.Immediate -> "Immediate"
            TimerStart.Delayed -> "Delayed"
            TimerStart.ParentStart -> "ParentStart"
            TimerStart.ParentEnd -> "ParentEnd"
        }
    }
}