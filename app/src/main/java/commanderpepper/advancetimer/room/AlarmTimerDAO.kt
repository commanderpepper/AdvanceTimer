package commanderpepper.advancetimer.room

import android.text.BoringLayout
import androidx.room.*

@Dao
interface AlarmTimerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimerGetID(alarmTimer: AlarmTimer): Long

    @Delete
    suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer)

    @Query("UPDATE AlarmTimer SET enabled = :alarmTimerEnabled WHERE id == :alarmTimerId")
    suspend fun modifyEnabledState(alarmTimerId: Int, alarmTimerEnabled: Boolean)

    @Query("UPDATE AlarmTimer SET timeInMillis = :newTriggerTime WHERE id == :alarmTimerId")
    suspend fun modifyTriggerTime(alarmTimerId: Int, newTriggerTime: Int)

    @Query("SELECT * FROM AlarmTimer")
    suspend fun getAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE id == :primaryId")
    suspend fun getAlarmTimer(primaryId: Int): AlarmTimer

    @Query("SELECT * FROM AlarmTimer WHERE parentID IS NULL")
    suspend fun getParentAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE parentID == :parentId")
    suspend fun getChildrenAlarmTimerList(parentId: Int): List<AlarmTimer>
}