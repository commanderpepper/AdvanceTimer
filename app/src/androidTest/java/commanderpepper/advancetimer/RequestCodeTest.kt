package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.App
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class RequestCodeTest {

    @Test
    fun getCode() {
        val applicationContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext

        val requestCodeGenerator = (applicationContext as App).appComponent.requestCodeGenerator()

        val int =
            requestCodeGenerator.getRequestCode()
        assertEquals(1, int)

        val secondInt =
            requestCodeGenerator.getRequestCode()
        assertEquals(2, secondInt)

        val thirdInt =
            requestCodeGenerator.getRequestCode()
        assertEquals(3, thirdInt)

    }
}