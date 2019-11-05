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
    fun insertAlarmTimer_AssertNotNull() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
//        val retrievedTimer = alarmTimerDao.getAlarmTimer(timer.id)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(retrievedTimer.id, CoreMatchers.notNullValue())
    }

    @Test
    fun insertManyAlarmTimers_GetAlarmTimerList_ListSizeGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(2))
    }

    @Test
    fun insertAlarmTimer_DeleteAlarmTimer_GetAlarmTimerList_ListSizeIsZero() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        alarmTimerDao.deleteAlarmTimer(timerList.first())
        val emptyTimer = alarmTimerDao.getAlarmTimerList()
        assertThat(emptyTimer.size, CoreMatchers.equalTo(0))
    }

    // TODO : Try to delete a timer not found on the database
    @Test
    fun deleteNonExistentTimer() = runBlocking {
        alarmTimerDao.deleteAlarmTimer(timer)
    }

    // TODO : Insert a timer and get a timer list of 1
    @Test
    fun insertAlarmTimer_GetAlarmTimerList_ListSizeIsOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(1))
    }

    // TODO : Get a timer list and expect an empty list
    @Test
    fun insertNothing_getEmptyAlarmTimerList() = runBlocking {
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(0))
    }

    // TODO: Insert a timer and get a timer with the ID. Should have similar properties.
    @Test
    fun insertAlarmTimer_GetAlarmTimerWithPrimaryID() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer, CoreMatchers.notNullValue())
        assertThat(timer.title, CoreMatchers.equalTo(retrievedTimer.title))
    }

    // TODO: Get a timer and get an error I guess
    @Test
    fun insertNoAlarmTimers_GetNoAlarmTimerWithPrimaryID() = runBlocking {
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer, CoreMatchers.nullValue())
    }

    // TODO: Insert a parent timer and get a timer with null has the parent ID parameter
    @Test
    fun insertParentAlarmTimer_GetParentAlarmTimer() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimer = alarmTimerDao.getParentAlarmTimerList().first()
        assertThat(parentTimer.title, CoreMatchers.equalTo(timer.title))
    }

    // TODO: Insert multiple parent timers
    @Test
    fun insertMultipleParentAlarmTimers_getAlarmTimerList_ListSizeIsGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(2))
    }

    // TODO: Insert no timers and get an empty list
    @Test
    fun insertNoAlarmTimers_GetEmptyParentAlarmTimersList() = runBlocking {
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(0))
    }

    // TODO: Insert a child timer and get an empty list
    @Test
    fun insertChildAlarmTimer_GetEmptyParentAlarmTimerList() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(0))
    }

    // TODO: Insert a child timer and get a child timer of list of size 1
    @Test
    fun insertChildAlarmTimer_GetAlarmTimerListOfOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(1))
    }

    // TODO: Insert many child timers and get a list of sizer greater than 1
    @Test
    fun insertManyChildAlarmTimers_GetListOfChildTimers_ListSizeGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        alarmTimerDao.insertAlarmTimer(childTimer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(2))
    }

    // TODO: Insert no child timers and get an empty list
    @Test
    fun insertNoChildAlarmTimers_GetChildAlarmTimerList_ListIsEmpty() = runBlocking {
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(0))
    }

    // TODO: Insert a parent timer and get an empty child timer list
    @Test
    fun insertParentAlarmTimer_GetChildTimerList_ListIsEmpty() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(0))
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
            1,
            2
        )
    }

}