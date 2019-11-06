package commanderpepper.advancetimer

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
import commanderpepper.advancetimer.room.AlarmTimer
import commanderpepper.advancetimer.room.AlarmTimerDAO
import commanderpepper.advancetimer.room.AlarmTimerDatabase
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


    @Before
    fun createDb() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

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
    fun insertAlarmTimerWithGeneratedRequestCode_GetAlarmTimerWithRequestCode() = runBlocking {
        val alarmTimer = AlarmTimer(
            "Test Alarm Timer",
            "TEST",
            true,
            null,
            RequestCodeGenerator.getRequestCode(context)
        )
        alarmTimerDao.insertAlarmTimer(
            alarmTimer
        )
        val retrievedTimer = alarmTimerDao.getAlarmTimer(1)
        assertThat(retrievedTimer.requestCode, CoreMatchers.equalTo(alarmTimer.requestCode))
    }

    @Test
    fun insertTwoAlarmTimersWithGeneratedRequestCode_GetAlarmTimerWithRequestCodeAsTwo() =
        runBlocking {
            val alarmTimerRQOne = AlarmTimer(
                "Test Alarm Timer",
                "TEST",
                true,
                null,
                RequestCodeGenerator.getRequestCode(context)
            )
            val alarmTimerRQTwo = AlarmTimer(
                "Test Alarm Timer",
                "TEST",
                true,
                null,
                RequestCodeGenerator.getRequestCode(context)
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