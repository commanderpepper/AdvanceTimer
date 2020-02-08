package commanderpepper.advancetimer.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlarmTimer")
data class AlarmTimer(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "enabled")
    val enabled: Boolean,
    @ColumnInfo(name = "parentID")
    val parentID: Int?,
    @ColumnInfo(name = "requestCode")
    val requestCode: Int,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0
)
