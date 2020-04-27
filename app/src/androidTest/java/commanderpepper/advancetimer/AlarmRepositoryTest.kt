package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.App
import commanderpepper.advancetimer.model.TimerStart
import commanderpepper.advancetimer.model.toMilliseconds
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
class AlarmRepositoryTest {
    private lateinit var alarmRepository: AlarmRepository

    @Before
    fun createAlarmRepo() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        alarmRepository = AlarmRepository(context)
        alarmRepository.setDatabaseForTesting()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        alarmRepository.closeDatabase()
    }

    @Test
    fun insertTimer_GetTimer_CheckNotNull() = runBlocking {
        alarmRepository.insertAlarmTimerGetId(timerA)
        val timerFromDB = alarmRepository.getAlarmTimer(1)

        assertThat(timerFromDB.title, CoreMatchers.equalTo(timerA.title))
    }

    companion object {
        //Timer A is the parent Timer
        private val timerA =
            AlarmTimer(
                "Timer A",
                AlarmTimerType.OneOffTimer,
                TimerStart.Immediate,
                true,
                0.toMilliseconds(),
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
            0.toMilliseconds(),
            0,
            3
        )
    }

}