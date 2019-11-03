package commanderpepper.advancetimer

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDAO
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import kotlinx.coroutines.*
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AlarmTimerDatabaseTest {

    private lateinit var alarmTimerDao: AlarmTimerDAO
    private lateinit var db: AlarmTimerDatabase


    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AlarmTimerDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        alarmTimerDao = db.alarmTimerDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun createAlarmTimer() {
        val testTimer = AlarmTimer(
            "te",
            "al",
            true,
            null,
            1
        )
        assertThat(testTimer.id, CoreMatchers.equalTo(0))
    }

    @Test
    fun insertAlarmTimer() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
//        val retrievedTimer = alarmTimerDao.getAlarmTimer(timer.id)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(retrievedTimer.id, CoreMatchers.notNullValue())
    }

    @Test
    fun insertSameTimerTwice() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(2))
    }

    @Test
    fun insertThenDeleteTimer() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        alarmTimerDao.deleteAlarmTimer(timerList.first())
        val emptyTimer = alarmTimerDao.getAlarmTimerList()
        assertThat(emptyTimer.size, CoreMatchers.equalTo(0))
    }

    // TODO : Try to delete a timer not found on the database
    fun deleteNonExistentTimer() = runBlocking {

    }

    // TODO : Insert a timer and get a timer list of 1
    fun insertTimerAndGetTimerList() = runBlocking {

    }

    // TODO : Insert many timers and get a timer list size great than 1
    fun insertManyTimersAndGetTimerList() = runBlocking {

    }

    // TODO : Get a timer list and expect an empty list
    fun getEmptyTimerList() = runBlocking {

    }

    @Test
    fun insertTwoTimers() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(childTimer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(2))
    }



    @Test
    fun insertAlarmTimerCheckForId() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
//        val retrievedTimer = alarmTimerDao.getAlarmTimer(timer.id)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
//        assertThat(retrievedTimer.id, CoreMatchers.not(0))
        assertThat(retrievedTimer.id, CoreMatchers.equalTo(1))
    }

    @Test
    fun insertTimerAndGetParentTimers() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimers = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimers.size, CoreMatchers.equalTo(1))
    }


    fun insertParentAndChildTimer() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimer = alarmTimerDao.getParentAlarmTimerList().first()
        val childTimer = AlarmTimer(
            "child",
            "Alarm",
            false,
            parentTimer.id,
            2
        )
        alarmTimerDao.insertAlarmTimer(childTimer)
        val childTimers = alarmTimerDao.getChildrenAlarmTimerList(parentId = parentTimer.id)
        assertThat(childTimers.size, CoreMatchers.equalTo(1))
    }

    companion object {
        private val timer = AlarmTimer(
            "test",
            "ALARM",
            true,
            null,
            1
        )
        private val childTimer = AlarmTimer(
            "test",
            "ALARM",
            true,
            null,
            2
        )
    }

}