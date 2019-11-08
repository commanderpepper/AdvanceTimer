package commanderpepper.advancetimer

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import org.junit.Before
import org.junit.runner.RunWith


/**
 * Test Alarm Creation
 */
@RunWith(AndroidJUnit4::class)
class AlarmCreatorTest{
    private lateinit var context : Context
    private lateinit var alarmCreator: AlarmCreator

    @Before
    fun init(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        alarmCreator = AlarmCreator(context)
    }
}