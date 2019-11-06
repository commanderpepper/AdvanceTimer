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
        val int =
            RequestCodeGenerator.getRequestCode(context)
        assertEquals(1, int)

        val secondInt =
            RequestCodeGenerator.getRequestCode(context)
        assertEquals(2, secondInt)

        val thirdInt =
            RequestCodeGenerator.getRequestCode(context)
        assertEquals(3, thirdInt)

    }
}