package commanderpepper.advancetimer

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.toMilliseconds
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDAO
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import commanderpepper.advancetimer.room.AlarmTimerType
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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

    // Test AlarmTimer creation
    @Test
    fun createAlarmTimer() {
        val testTimer = AlarmTimer(
            "te",
            AlarmTimerType.OneOffAlarm,
            TimerStart.ParentStart,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            null
        )
        assertThat(testTimer.id, CoreMatchers.equalTo(0))
    }

    // Insert an AlarmTimer and retrieve one
    @Test
    fun insertAlarmTimer_AssertNotNull() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
//        val retrievedTimer = alarmTimerDao.getAlarmTimer(timer.id)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(retrievedTimer.id, CoreMatchers.notNullValue())
    }

    // Insert multiple AlarmTimers and retrieve a list of them
    @Test
    fun insertManyAlarmTimers_GetAlarmTimerList_ListSizeGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(2))
    }

    // Insert AlarmTimer, the delete it, then get an empty list
    @Test
    fun insertAlarmTimer_DeleteAlarmTimer_GetAlarmTimerList_ListSizeIsZero() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        alarmTimerDao.deleteAlarmTimer(timerList.first())
        val emptyTimer = alarmTimerDao.getAlarmTimerList()
        assertThat(emptyTimer.size, CoreMatchers.equalTo(0))
    }

    // Try to delete a timer not found on the database
    @Test
    fun deleteNonExistentTimer() = runBlocking {
        alarmTimerDao.deleteAlarmTimer(timer)
    }

    // Insert a timer and get a timer list of 1
    @Test
    fun insertAlarmTimer_GetAlarmTimerList_ListSizeIsOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(1))
    }

    // Get a timer list and expect an empty list
    @Test
    fun insertNothing_getEmptyAlarmTimerList() = runBlocking {
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(0))
    }

    // Insert a timer and get a timer with the ID. Should have similar properties.
    @Test
    fun insertAlarmTimer_GetAlarmTimerWithPrimaryID() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer, CoreMatchers.notNullValue())
        assertThat(timer.title, CoreMatchers.equalTo(retrievedTimer.title))
    }

    // Get a timer and get an error
    @Test
    fun insertNoAlarmTimers_GetNoAlarmTimerWithPrimaryID() = runBlocking {
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer, CoreMatchers.nullValue())
    }

    // Insert a parent timer and get a timer with null has the parent ID parameter
    @Test
    fun insertParentAlarmTimer_GetParentAlarmTimer() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimer = alarmTimerDao.getParentAlarmTimerList().first()
        assertThat(parentTimer.title, CoreMatchers.equalTo(timer.title))
    }

    // Insert multiple parent timers
    @Test
    fun insertMultipleParentAlarmTimers_getAlarmTimerList_ListSizeIsGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(2))
    }

    // Insert no timers and get an empty list
    @Test
    fun insertNoAlarmTimers_GetEmptyParentAlarmTimersList() = runBlocking {
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(0))
    }

    // Insert a child timer and get an empty list
    @Test
    fun insertChildAlarmTimer_GetEmptyParentAlarmTimerList() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        val parentTimerList = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimerList.size, CoreMatchers.equalTo(0))
    }

    // Insert a child timer and get a child timer of list of size 1
    @Test
    fun insertChildAlarmTimer_GetAlarmTimerListOfOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(1))
    }

    // Insert many child timers and get a list of sizer greater than 1
    @Test
    fun insertManyChildAlarmTimers_GetListOfChildTimers_ListSizeGreaterThanOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(childTimer)
        alarmTimerDao.insertAlarmTimer(childTimer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(2))
    }

    // Insert no child timers and get an empty list
    @Test
    fun insertNoChildAlarmTimers_GetChildAlarmTimerList_ListIsEmpty() = runBlocking {
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(0))
    }

    // Insert a parent timer and get an empty child timer list
    @Test
    fun insertParentAlarmTimer_GetChildTimerList_ListIsEmpty() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val retrievedChildAlarmTimer = alarmTimerDao.getChildrenAlarmTimerList(1)
        assertThat(retrievedChildAlarmTimer.size, CoreMatchers.equalTo(0))
    }

    // Insert two timers and get a list of size 2
    @Test
    fun insertTwoAlarmTimers_GetAlarmTimerList_ListSizeIsTwo() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        alarmTimerDao.insertAlarmTimer(childTimer)
        val timerList = alarmTimerDao.getAlarmTimerList()
        assertThat(timerList.size, CoreMatchers.equalTo(2))
    }

    // Insert a timer, check for id.
    @Test
    fun insertAlarmTimer_GetAlarmTimer_CheckForId() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
//        val retrievedTimer = alarmTimerDao.getAlarmTimer(timer.id)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
//        assertThat(retrievedTimer.id, CoreMatchers.not(0))
        assertThat(retrievedTimer.id, CoreMatchers.equalTo(1))
    }

    // Insert parent timer and get a parent timer list of 1
    @Test
    fun insertAlarmTimer_GetParentTimersList_ListSizeIsOne() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val parentTimers = alarmTimerDao.getParentAlarmTimerList()
        assertThat(parentTimers.size, CoreMatchers.equalTo(1))
    }


    // Insert a parent timer, a child timer using the parent timer's id, get a child timer list of one
    @Test
    fun insertParentAlarmTimer_InsertChildAlarmTimer_GetChildAlarmTimerList_ListSizeIsOne() =
        runBlocking {
            alarmTimerDao.insertAlarmTimer(timer)
            val parentTimer = alarmTimerDao.getParentAlarmTimerList().first()
            val childTimer = AlarmTimer(
                "child",
                AlarmTimerType.OneOffAlarm,
                TimerStart.ParentStart,
                false,
                1.toMilliseconds(), 0.toMilliseconds(), 0,
                parentTimer.id
            )
            alarmTimerDao.insertAlarmTimer(childTimer)
            val childTimers = alarmTimerDao.getChildrenAlarmTimerList(parentId = parentTimer.id)
            assertThat(childTimers.size, CoreMatchers.equalTo(1))
        }

    @Test
    fun insertAlarmTimerWithEnabledState_DisableTimer_CheckIfDisabled() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        alarmTimerDao.modifyEnabledState(retrievedTimer.id, false)
        val disabledTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(disabledTimer.enabled, CoreMatchers.equalTo(false))
    }

    @Test
    fun insertAlarmTimerWithDisaledState_EnableTimer_CheckIfEnabled() = runBlocking {
        val testTimer = AlarmTimer(
            "test",
            AlarmTimerType.OneOffTimer,
            TimerStart.ParentStart,
            false,
            1.toMilliseconds(),
            0.toMilliseconds(),
            0,
            null
        )
        alarmTimerDao.insertAlarmTimer(testTimer)
        val retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        alarmTimerDao.modifyEnabledState(retrievedTimer.id, true)
        val disabledTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(disabledTimer.enabled, CoreMatchers.equalTo(true))
    }

    @Test
    fun insertTimer_ModifyTriggerTimer_CheckIfTriggerTimeIsDifferent() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        var retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        alarmTimerDao.modifyTriggerTime(retrievedTimer.id, 3)
        retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(retrievedTimer, CoreMatchers.not(timer))
    }

    @Test
    fun insertTimer_ModifyTriggerTime_CheckIfTimerForSpecificValue() = runBlocking {
        alarmTimerDao.insertAlarmTimer(timer)
        var retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        alarmTimerDao.modifyTriggerTime(retrievedTimer.id, 5)
        retrievedTimer = alarmTimerDao.getAlarmTimerList().first()
        assertThat(retrievedTimer.triggerTime.amount, CoreMatchers.equalTo(5.toLong()))
    }

    fun insertTimers_GetTimerGraph() = runBlocking {
        val timerList = listOf(timerA, timerB, timerC, timerD, timerE, timerF)
        timerList.forEach {
            alarmTimerDao.insertAlarmTimer(it)
        }
    }

    @Test
    fun insertTimers_CheckForEnabledTimer() = runBlocking {
        val timerA = AlarmTimer(
            "test",
            AlarmTimerType.OneOffAlarm,
            TimerStart.ParentStart,
            false,
            1.toMilliseconds(),
            0.toMilliseconds(),
            0,
            null
        )
        val timerB = AlarmTimer(
            "Timer B",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            1
        )
        val timerC = AlarmTimer(
            "Timer C",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            1
        )

        alarmTimerDao.insertAlarmTimer(timerA)
        alarmTimerDao.insertAlarmTimer(timerB)
        alarmTimerDao.insertAlarmTimer(timerC)

        val list = alarmTimerDao.getAllEnabledTimers()
        assertTrue(list.size == 2)
    }

    companion object {
        private val timer = AlarmTimer(
            "test",
            AlarmTimerType.OneOffAlarm,
            TimerStart.ParentStart,
            true,
            1.toMilliseconds(),
            0.toMilliseconds(),
            0,
            null
        )
        private val childTimer = AlarmTimer(
            "test",
            AlarmTimerType.OneOffAlarm,
            TimerStart.ParentStart,
            true,
            2.toMilliseconds(),
            1.toMilliseconds(),
            3,
            1
        )

        //Timer A is the parent Timer
        private val timerA =
            AlarmTimer(
                "Timer A",
                AlarmTimerType.OneOffTimer,
                TimerStart.Immediate,
                true,
                0.toMilliseconds(),
                0.toMilliseconds(),
                0,
                null
            )

        //Timer B is a child of timer A
        private val timerB = AlarmTimer(
            "Timer B",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            1
        )

        //Timer C is a child of timer A
        private val timerC = AlarmTimer(
            "Timer C",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            1
        )

        //Timer D is a child of timer A
        private val timerD = AlarmTimer(
            "Timer D",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            1
        )

        //Timer E is a child of timer B
        private val timerE = AlarmTimer(
            "Timer E",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            2
        )

        //Timer F is a child of timer C
        val timerF = AlarmTimer(
            "Timer F",
            AlarmTimerType.OneOffTimer,
            TimerStart.Immediate,
            true,
            0.toMilliseconds(),
            0.toMilliseconds(),
            0,
            3
        )

    }

}