package commanderpepper.advancetimer.room

import android.text.BoringLayout
import androidx.room.*
import commanderpepper.advancetimer.model.UnitsOfTime

@Dao
interface AlarmTimerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimerGetID(alarmTimer: AlarmTimer): Long

    @Delete
    suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer)

    @Query("SELECT COUNT (1) FROM alarmtimer WHERE id == :alarmTimerId ")
    suspend fun checkForTimer(alarmTimerId: Int): Int

    @Query("DELETE FROM alarmtimer WHERE id == :alarmTimerId ")
    suspend fun deleteTimer(alarmTimerId: Int)

    @Query("DELETE FROM alarmtimer WHERE parentID == :parentId ")
    suspend fun deleteChildTimers(parentId: Int)

    @Query("UPDATE AlarmTimer SET enabled = :alarmTimerEnabled WHERE id == :alarmTimerId")
    suspend fun modifyEnabledState(alarmTimerId: Int, alarmTimerEnabled: Boolean)

    @Query("UPDATE AlarmTimer SET triggerTime = :newTriggerTime WHERE id == :alarmTimerId")
    suspend fun modifyTriggerTime(alarmTimerId: Int, newTriggerTime: Int)

    @Query("UPDATE AlarmTimer SET triggerTime = :newTriggerTime WHERE id == :alarmTimerId")
    suspend fun modifyTriggerTime(alarmTimerId: Int, newTriggerTime: UnitsOfTime.MilliSecond)

    @Query("SELECT * FROM AlarmTimer")
    suspend fun getAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE id == :primaryId")
    suspend fun getAlarmTimer(primaryId: Int): AlarmTimer

    @Query("SELECT * FROM AlarmTimer WHERE parentID IS NULL")
    suspend fun getParentAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE parentID == :parentId")
    suspend fun getChildrenAlarmTimerList(parentId: Int): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE enabled == 1")
    suspend fun getAllEnabledTimers(): List<AlarmTimer>
}