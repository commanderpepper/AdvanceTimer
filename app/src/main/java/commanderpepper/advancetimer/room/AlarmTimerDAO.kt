package commanderpepper.advancetimer.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface AlarmTimerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer): Long

    @Delete
    suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer)
}