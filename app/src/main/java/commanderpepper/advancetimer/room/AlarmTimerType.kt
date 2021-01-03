package commanderpepper.advancetimer.room

import androidx.room.TypeConverter

sealed class AlarmTimerType {
    object OneOffTimer : AlarmTimerType()
    object RepeatingTimer : AlarmTimerType()
}

class AlarmTimerTypeConverter {

    @TypeConverter
    fun fromAlarmTimerType(value: AlarmTimerType): String {
        return when (value) {
            AlarmTimerType.OneOffTimer -> "OneOffTimer"
            AlarmTimerType.RepeatingTimer -> "RepeatingTimer"
        }
    }

    @TypeConverter
    fun stringToAlarmTimerType(string: String): AlarmTimerType {
        return when (string) {
            "OneOffTimer" -> AlarmTimerType.OneOffTimer
            "RepeatingTimer" -> AlarmTimerType.RepeatingTimer
            else -> throw Error()
        }
    }
}

fun AlarmTimerType.getTypeAsString(): String {
    return when (this) {
        AlarmTimerType.OneOffTimer -> "One Off Timer"
        AlarmTimerType.RepeatingTimer -> "Repeating Timer"
    }
}