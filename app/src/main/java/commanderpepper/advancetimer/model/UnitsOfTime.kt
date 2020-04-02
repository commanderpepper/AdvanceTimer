package commanderpepper.advancetimer.model


sealed class UnitsOfTime() {
    data class Hour(val amount: Long) : UnitsOfTime()
    data class Minute(val amount: Long) : UnitsOfTime()
    data class Second(val amount: Long) : UnitsOfTime()
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
