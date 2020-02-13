package commanderpepper.advancetimer.room

import androidx.room.TypeConverter

sealed class AlarmTimerType {
    object OneOffAlarm : AlarmTimerType()
    object OneOffTimer : AlarmTimerType()
    object RepeatingTimer : AlarmTimerType()
    object RepeatingAlarm : AlarmTimerType()
}

class AlarmTimerTypeConverter {

    @TypeConverter
    fun fromAlarmTimerType(value: AlarmTimerType): String {
        return when (value) {
            AlarmTimerType.OneOffAlarm -> "OneOffAlarm"
            AlarmTimerType.OneOffTimer -> "OneOffTimer"
            AlarmTimerType.RepeatingAlarm -> "RepeatingAlarm"
            AlarmTimerType.RepeatingTimer -> "RepeatingTimer"
        }
    }

    @TypeConverter
    fun stringToAlarmTimerType(string: String): AlarmTimerType {
        return when (string) {
            "OneOffAlarm" -> AlarmTimerType.OneOffAlarm
            "OneOffTimer" -> AlarmTimerType.OneOffTimer
            "RepeatingAlarm" -> AlarmTimerType.RepeatingAlarm
            "RepeatingTimer" -> AlarmTimerType.RepeatingTimer
            else -> throw Error()
        }
    }
}