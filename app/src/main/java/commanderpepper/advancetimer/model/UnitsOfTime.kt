package commanderpepper.advancetimer.model


sealed class UnitsOfTime() {
    data class Hour(val amount: Long) : UnitsOfTime()
    data class Minute(val amount: Long) : UnitsOfTime()
    data class Second(val amount: Long) : UnitsOfTime()
    data class MilliSeconds(val amount: Long) : UnitsOfTime()
}

fun UnitsOfTime.toMilliseconds(): UnitsOfTime.MilliSeconds {
    return when (this) {
        is UnitsOfTime.Hour -> UnitsOfTime.MilliSeconds(this.amount * 3_600_000L)
        is UnitsOfTime.Minute -> UnitsOfTime.MilliSeconds(this.amount * 60_000L)
        is UnitsOfTime.Second -> UnitsOfTime.MilliSeconds(this.amount * 1_000L)
        is UnitsOfTime.MilliSeconds -> UnitsOfTime.MilliSeconds(this.amount)
    }
}