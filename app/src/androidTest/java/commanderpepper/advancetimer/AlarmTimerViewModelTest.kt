package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import org.junit.Before

class AlarmTimerViewModelTest {

    private lateinit var alarmTimerViewModel: AlarmTimerViewModel

    @Before
    fun setUp(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val alarmRepository = AlarmRepository(context)

//        alarmTimerViewModel = AlarmTimerViewModel()
    }

}