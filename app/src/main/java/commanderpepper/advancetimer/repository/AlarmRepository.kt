package commanderpepper.advancetimer.repository

import android.content.Context
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import timber.log.Timber
import javax.inject.Inject

class AlarmRepository @Inject constructor(val context: Context) {
    private val database = AlarmTimerDatabase.getInstance(context)
    private val alarmTimerDAO = database.alarmTimerDAO()

    suspend fun getParentTimersFlow(): Flow<AlarmTimer> {
        Timber.d("Getting parent timers")
        return alarmTimerDAO.getParentAlarmTimerList().asFlow()
    }

    suspend fun getParentTimersList(): List<AlarmTimer> {
        return alarmTimerDAO.getParentAlarmTimerList()
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

    suspend fun insertAlarmTimerGetId(alarmTimer: AlarmTimer): Long {
        return alarmTimerDAO.insertAlarmTimerGetID(alarmTimer)
    }
}