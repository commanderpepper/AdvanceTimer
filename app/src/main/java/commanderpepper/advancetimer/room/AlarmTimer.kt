package commanderpepper.advancetimer.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlarmTimer")
data class AlarmTimer(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "type")
    val type: AlarmTimerType,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    @ColumnInfo(name = "timeInMillis")
    val timeInMillis: Long,
    @ColumnInfo(name = "intervalAtMillis")
    val intervalAtMillis: Long = 0L,
    @ColumnInfo(name = "requestCode")
    var requestCode: Int,
    @ColumnInfo(name = "parentID")
    val parentID: Int?,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
)
