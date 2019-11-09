package commanderpepper.advancetimer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.receivers.AlarmReceiver
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertThat


/**
 * Test Alarm Creation
 */
@RunWith(AndroidJUnit4::class)
class AlarmCreatorTest {
    private lateinit var context: Context
    private lateinit var alarmCreator: AlarmCreator

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        alarmCreator = AlarmCreator(context)
    }

    @Test
    fun createPendingIntent_RetrievePendingIntent_CheckIfEqual() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
            .putExtra("Source", "Test")

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

//        alarmCreator.makeAlarm(alarmIntent, 10000L)

        val retrievedIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)


        assertThat(alarmIntent, CoreMatchers.equalTo(retrievedIntent))
    }
}