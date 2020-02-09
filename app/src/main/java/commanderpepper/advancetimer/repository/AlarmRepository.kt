package commanderpepper.advancetimer.repository

import android.content.Context
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class AlarmRepository private constructor(private val context: Context) {
    private val database = AlarmTimerDatabase.getInstance(context)
    private val alarmTimerDAO = database.alarmTimerDAO()

    suspend fun getParentTimers(): Flow<AlarmTimer> {
        return alarmTimerDAO.getParentAlarmTimerList().asFlow()
    }

    suspend fun getChildrenTimers(parentId: Int): Flow<AlarmTimer> {
        return alarmTimerDAO.getChildrenAlarmTimerList(parentId).asFlow()
    }

    suspend fun getAlarmTimer(alarmTimerId: Int): AlarmTimer {
        return alarmTimerDAO.getAlarmTimer(alarmTimerId)
    }

    suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer) {
        alarmTimerDAO.deleteAlarmTimer(alarmTimer)
    }

    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer) {
        alarmTimerDAO.insertAlarmTimer(alarmTimer)
    }

    companion object {
        private var INSTANCE: AlarmRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = AlarmRepository(context)
        }

        fun get(): AlarmRepository {
            return INSTANCE ?: throw IllegalArgumentException("CrimeRepository must be initialized")
        }
    }
}