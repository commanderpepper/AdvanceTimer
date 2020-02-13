package commanderpepper.advancetimer

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDAO
import commanderpepper.advancetimer.room.AlarmTimerDatabase
import commanderpepper.advancetimer.room.AlarmTimerType
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RequestCodeDatabaseTest {
    private lateinit var alarmTimerDao: AlarmTimerDAO
    private lateinit var db: AlarmTimerDatabase
    private lateinit var context: Context
    private lateinit var requestCodeGenerator: RequestCodeGenerator


    @Before
    fun createDb() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, AlarmTimerDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        alarmTimerDao = db.alarmTimerDAO()

        requestCodeGenerator = RequestCodeGenerator.get()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAlarmTimerWithGeneratedRequestCode_GetAlarmTimerWithRequestCode() = runBlocking {
        val alarmTimer = AlarmTimer(
            "Test Alarm Timer",
            AlarmTimerType.OneOffAlarm,
            true,
            requestCodeGenerator.getRequestCode(),
            null
        )
        alarmTimerDao.insertAlarmTimer(alarmTimer)
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer.requestCode, CoreMatchers.equalTo(alarmTimer.requestCode))
    }

    @Test
    fun insertTwoAlarmTimersWithGeneratedRequestCode_GetAlarmTimerWithRequestCodeAsTwo() =
        runBlocking {
            val alarmTimerRQOne = AlarmTimer(
                "Test Alarm Timer",
                AlarmTimerType.OneOffAlarm,
                true,
                requestCodeGenerator.getRequestCode(),
                1
            )
            val alarmTimerRQTwo = AlarmTimer(
                "Test Alarm Timer",
                AlarmTimerType.OneOffAlarm,
                true,
                requestCodeGenerator.getRequestCode(),
                2
            )
            alarmTimerDao.insertAlarmTimer(alarmTimerRQOne)
            alarmTimerDao.insertAlarmTimer(alarmTimerRQTwo)
            val secondRetrievedTimer = alarmTimerDao.getAlarmTimer(2)
            assertThat(
                secondRetrievedTimer.requestCode,
                CoreMatchers.equalTo(alarmTimerRQTwo.requestCode)
            )

        }
}