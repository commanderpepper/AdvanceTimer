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
    fun insertManyAlarmTimer_GetAlarmTimerList_ListSizeGreaterThanTwo() = runBlocking {
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
    fun deleteNonExistentTimer() = runBlocking {

    }

    // TODO : Insert a timer and get a timer list of 1
    fun insertAlarmTimer_GetAlarmTimerList_ListSizeIsOne() = runBlocking {

    }

    // TODO : Insert many timers and get a timer list size greater than 1
    fun insertManyAlarmTimers_GetAlarmTimerList_ListSizeIsGreaterThanOne() = runBlocking {

    }

    // TODO : Get a timer list and expect an empty list
    fun insertNothing_getEmptyAlarmTimerList() = runBlocking {

    }

    // TODO: Insert a timer and get a timer with the ID. Should have similar properties.
    fun insertAlarmTimer_GetAlarmTimerWithPrimaryID() = runBlocking {

    }

    // TODO: Get a timer and get an error I guess
    fun insertNoAlarmTimers_GetNoAlarmTimerWithPrimaryID() = runBlocking {

    }

    // TODO: Insert a parent timer and get a timer with null has the parent ID parameter
    fun insertParentAlarmTimer_GetParentAlarmTimer() = runBlocking {

    }

    // TODO: Insert multiple parent timers
    fun insertMultipleParentAlarmTimers_getAlarmTimerList_ListSizeIsGreaterThanOne() = runBlocking {

    }

    // TODO: Insert no timers and get an empty list
    fun insertNoAlarmTimers_GetEmptyParentAlarmTimersList() = runBlocking {

    }

    // TODO: Insert a child timer and get an empty list
    fun insertChildAlarmTimer_GetEmptyParentAlarmTimerList() = runBlocking {

    }

    // TODO: Insert a child timer and get a child timer of list of size 1
    fun insertChildAlarmTimer_GetAlarmTimerListOfOne() = runBlocking {

    }

    // TODO: Insert many child timers and get a list of sizer greater than 1
    fun insertManyChildAlarmTimers_GetListOfChildTimers_ListSizeGreaterThanOne() = runBlocking {

    }

    // TODO: Insert no child timers and get an empty list
    fun insertNoChildAlarmTimers_GetChildAlarmTimerList_ListIsEmpty() = runBlocking {

    }

    // TODO: Insert a parent timer and get an empty child timer list
    fun insertParentAlarmTimer_GetChildTimerList_ListIsEmpty() = runBlocking {

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