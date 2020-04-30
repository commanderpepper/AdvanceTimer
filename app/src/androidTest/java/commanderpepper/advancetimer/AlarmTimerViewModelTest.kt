package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.toMilliseconds
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimerType
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AlarmTimerViewModelTest {

    private lateinit var alarmTimerViewModel: AlarmTimerViewModel

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val alarmRepository = AlarmRepository(context)
        val alarmCreator = AlarmCreator(context.applicationContext)

        alarmTimerViewModel = AlarmTimerViewModel(alarmRepository, alarmCreator)
        alarmTimerViewModel.setUpAlarmRepoForTesting()
    }

    @Test
    fun createTimer_GetTimer() = runBlocking {
        val returnFlow = alarmTimerViewModel.createTimer(
            "Test",
            1L.toMilliseconds(),
            null,
            AlarmTimerType.OneOffTimer,
            2L.toMilliseconds(),
            4L.toMilliseconds(),
            TimerStart.ParentStart
        )
        val result = returnFlow.first()
        assertThat(result, CoreMatchers.equalTo(1))

        val retrievedTimer = alarmTimerViewModel.getAlarmTimer(1)
        assertThat(retrievedTimer.title, CoreMatchers.equalTo("Test"))
    }

    @Test
    fun createParentAndChildTimer_CheckForChildTimer() = runBlocking {
        val parentTimerFlow = alarmTimerViewModel.createTimer(
            "Parent",
            1L.toMilliseconds(),
            null,
            AlarmTimerType.OneOffTimer,
            2L.toMilliseconds(),
            4L.toMilliseconds(),
            TimerStart.ParentStart
        )

        val parentResult = parentTimerFlow.first()
        assertThat(parentResult, CoreMatchers.equalTo(1))

        val childTimerFlow = alarmTimerViewModel.createTimer(
            "Child",
            1L.toMilliseconds(),
            1,
            AlarmTimerType.OneOffTimer,
            2L.toMilliseconds(),
            4L.toMilliseconds(),
            TimerStart.ParentStart
        )

        val childResult = childTimerFlow.first()
        assertThat(childResult, CoreMatchers.equalTo(2))

        val childTimers = alarmTimerViewModel.getChildAlarmTimers(1)
        assertTrue(childTimers.isNotEmpty())
        assertThat(childTimers.first().title, CoreMatchers.equalTo("Child"))

    }

    @ExperimentalStdlibApi
    @Test
    fun createTimer_DeleteTimer_CheckForNoTimer() = runBlocking {
        val returnFlow = alarmTimerViewModel.createTimer(
            "Test",
            1L.toMilliseconds(),
            null,
            AlarmTimerType.OneOffTimer,
            2L.toMilliseconds(),
            4L.toMilliseconds(),
            TimerStart.ParentStart
        )
        val result = returnFlow.first()
        assertThat(result, CoreMatchers.equalTo(1))

        alarmTimerViewModel.deleteTimer(1)
        
        val timerList = alarmTimerViewModel.getParentAlarmTimers()
        assertTrue(timerList.isEmpty())
    }
}