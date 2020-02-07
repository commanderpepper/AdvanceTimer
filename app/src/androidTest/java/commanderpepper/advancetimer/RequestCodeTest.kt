package commanderpepper.advancetimer

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class RequestCodeTest {

    @Test
    fun getCode() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        RequestCodeGenerator.initialize(context)
        val requestCodeGenerator = RequestCodeGenerator.get()
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