package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.App
import commanderpepper.advancetimer.repository.AlarmRepository
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmRepositoryTest {
    private lateinit var alarmRepository: AlarmRepository

    @Before
    fun createAlarmRepo(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
    }

}