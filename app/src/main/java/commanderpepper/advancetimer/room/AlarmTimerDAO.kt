package commanderpepper.advancetimer.room

import androidx.room.*

@Dao
interface AlarmTimerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer)

    @Delete
    suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer)

    @Query("SELECT * FROM AlarmTimer")
    suspend fun getAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE id == :primaryId")
    suspend fun getAlarmTimer(primaryId: Int): AlarmTimer

    @Query("SELECT * FROM AlarmTimer WHERE parentID IS NULL")
    suspend fun getParentAlarmTimerList(): List<AlarmTimer>

    @Query("SELECT * FROM AlarmTimer WHERE parentID == :parentId")
    suspend fun getChildrenAlarmTimerList(parentId: Int): List<AlarmTimer>
}