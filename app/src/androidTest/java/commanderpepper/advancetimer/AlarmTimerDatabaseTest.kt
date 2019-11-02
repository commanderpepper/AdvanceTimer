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
    fun insertAlarmTimerTest() = runBlocking {
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
    fun insertTwoTimers() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(childTimer)
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