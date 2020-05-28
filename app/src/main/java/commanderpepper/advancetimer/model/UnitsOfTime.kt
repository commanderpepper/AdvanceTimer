package commanderpepper.advancetimer.model

import androidx.room.TypeConverter
import java.util.*


sealed class UnitsOfTime {
    data class Hour(val amount: Long) : UnitsOfTime() {
        fun toInt() = amount.toInt()
    }

    data class Minute(val amount: Long) : UnitsOfTime() {
        fun toInt() = amount.toInt()
    }

    data class Second(val amount: Long) : UnitsOfTime() {
        fun toInt() = amount.toInt()
    }

    data class MilliSecond(val amount: Long) : UnitsOfTime()
}

fun UnitsOfTime.toMillisecond(): UnitsOfTime.MilliSecond {
    return when (this) {
        is UnitsOfTime.Hour -> UnitsOfTime.MilliSecond(this.amount * 3_600_000L)
        is UnitsOfTime.Minute -> UnitsOfTime.MilliSecond(this.amount * 60_000L)
        is UnitsOfTime.Second -> UnitsOfTime.MilliSecond(this.amount * 1_000L)
        is UnitsOfTime.MilliSecond -> UnitsOfTime.MilliSecond(this.amount)
    }
}

operator fun UnitsOfTime.MilliSecond.plus(other: UnitsOfTime.MilliSecond): UnitsOfTime.MilliSecond {
    return UnitsOfTime.MilliSecond(this.amount + other.amount)
}

fun Long.toMilliseconds() = UnitsOfTime.MilliSecond(this)

fun Int.toMilliseconds() = UnitsOfTime.MilliSecond(this.toLong())

/**
 * The trigger time will be the time of alarm creation plus whatever the user inputted.
 */
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

class UnitsOfTimeConverter {

    @TypeConverter
    fun longToUnitsOfTime(amount: Long) = UnitsOfTime.MilliSecond(amount)

    @TypeConverter
    fun unitsOfTimeToLong(unitsOfTime: UnitsOfTime.MilliSecond) = unitsOfTime.amount
}