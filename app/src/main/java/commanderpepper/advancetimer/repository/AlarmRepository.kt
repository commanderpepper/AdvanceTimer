package commanderpepper.advancetimer.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import commanderpepper.advancetimer.model.UnitsOfTime
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import timber.log.Timber
import javax.inject.Inject

class AlarmRepository @Inject constructor(val context: Context) {
    private var database = AlarmTimerDatabase.getInstance(context)
    private var alarmTimerDAO = database.alarmTimerDAO()

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

    private suspend fun deleteAlarmTimer(alarmTimer: AlarmTimer) {
        alarmTimerDAO.deleteAlarmTimer(alarmTimer)
    }

    suspend fun insertAlarmTimer(alarmTimer: AlarmTimer) {
        alarmTimerDAO.insertAlarmTimer(alarmTimer)
    }

    suspend fun insertAlarmTimerGetId(alarmTimer: AlarmTimer): Long {
        return alarmTimerDAO.insertAlarmTimerGetID(alarmTimer)
    }

    suspend fun modifyAlarmTimerEnabledState(alarmTimerId: Int) {
        val alarmTimer = getAlarmTimer(alarmTimerId)
        if (alarmTimer.type == AlarmTimerType.OneOffTimer) {
            disableAlarmTimer(alarmTimerId)
        }
    }

    suspend fun disableAlarmTimer(alarmTimerId: Int) {
        alarmTimerDAO.modifyEnabledState(alarmTimerId, false)
    }

    suspend fun enableAlarmTimer(
        alarmTimerId: Int,
        newTriggerTime: UnitsOfTime.MilliSecond
    ) {
        alarmTimerDAO.modifyEnabledState(alarmTimerId, true)
        alarmTimerDAO.modifyTriggerTime(alarmTimerId, newTriggerTime)
    }

    suspend fun checkForTimer(alarmTimerId: Int): Boolean {
        return alarmTimerDAO.checkForTimer(alarmTimerId) == 1
    }

    @ExperimentalStdlibApi
    suspend fun deleteTimer(alarmTimerId: Int) {
        val timerGraph = getAllTimersRelatedToParentTimer(alarmTimerId)
        timerGraph.forEach {
            deleteAlarmTimer(it)
        }
    }

    @ExperimentalStdlibApi
    suspend fun getAllTimersRelatedToParentTimer(parentId: Int): List<AlarmTimer> {
        val rootParentTimer = alarmTimerDAO.getAlarmTimer(parentId)

        val childTimers =
            alarmTimerDAO.getChildrenAlarmTimerList(parentId).toMutableList()

        val stack = mutableListOf<AlarmTimer>()
        stack.addAll(childTimers)

        while (stack.isNotEmpty()) {
            val firstTimer = stack.first()
            stack.removeFirst()

            val firstChildTimers = alarmTimerDAO.getChildrenAlarmTimerList(firstTimer.id)
            stack.addAll(firstChildTimers)
            childTimers.addAll(firstChildTimers)
        }

        childTimers.add(0, rootParentTimer)
        return childTimers
    }

    @VisibleForTesting
    fun setDatabaseForTesting() {
        database = Room.inMemoryDatabaseBuilder(context, AlarmTimerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        alarmTimerDAO = database.alarmTimerDAO()
    }

    @VisibleForTesting
    fun closeDatabase() {
        database.close()
    }
}