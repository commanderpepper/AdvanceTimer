package commanderpepper.advancetimer.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.UnitsOfTime
import java.util.*

@Entity(tableName = "AlarmTimer")
data class AlarmTimer(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "type")
    val type: AlarmTimerType,
    @ColumnInfo(name = "timerStart")
    val timerStart: TimerStart,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    @ColumnInfo(name = "delayTime")
    val delayTime: UnitsOfTime.MilliSecond,
    @ColumnInfo(name = "repeatTime")
    val repeatTime: UnitsOfTime.MilliSecond,
    @ColumnInfo(name = "time")
    val time: UnitsOfTime.MilliSecond,
    @ColumnInfo(name = "parentID")
    val parentID: Int?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
)
