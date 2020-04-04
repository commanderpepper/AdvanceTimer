package commanderpepper.advancetimer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import commanderpepper.App
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.alarmcreation.RequestCodeGenerator
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
    private lateinit var requestCodeGenerator: RequestCodeGenerator

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        alarmCreator = AlarmCreator(context)
        requestCodeGenerator = (context as App).appComponent.requestCodeGenerator()
    }

    @Test
    fun createPendingIntent_RetrievePendingIntent_CheckIfNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        val retrievedIntent =
            PendingIntent.getActivities(
                context,
                0,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(retrievedIntent, CoreMatchers.notNullValue())

        try {
            alarmIntent.cancel()
            retrievedIntent.cancel()
        } finally {

        }
    }

    @Test
    fun createPendingIntent_RetrievePendingIntent_CheckIfEqual() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        val retrievedIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        assertThat(alarmIntent, CoreMatchers.equalTo(retrievedIntent))
    }

    @Test
    fun createDifferentPendingIntent_CheckIfNotEqual() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        val differentPendingIntent =
            PendingIntent.getActivities(context, 1, arrayOf(sourceIntent), 0)

        assertThat(alarmIntent, CoreMatchers.not(differentPendingIntent))
    }

    @Test
    fun createPendingIntentWthAlarmCreator_RetrievePendingIntent_TestNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeAlarm(sourceIntent, 100000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.notNullValue())
    }

    @Test
    fun createNothing_GetPendingIntent_CheckIfNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                0,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.nullValue())
    }

    @Test
    fun createTimer_RetrieveTimer_TestNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeTimer(sourceIntent, 100000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.notNullValue())
    }

    @Test
    fun createRepeatingAlarm_RetrieveAlarm_TestNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeRepeatingAlarm(sourceIntent, 100000L, 100000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.notNullValue())
    }

    @Test
    fun createRepeatingTimer_RetrieveTimer_TestNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeRepeatingTimer(sourceIntent, 100000L, 100000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.notNullValue())
    }

    @Test
    fun createTimer_Wait_RetrieveTimer_TestForNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeTimer(sourceIntent, 100L)

        Thread.sleep(2000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.nullValue())
    }

    @Test
    fun createAlarm_Wait_RetrieveAlarm_TestForNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeAlarm(sourceIntent, 100L)

        Thread.sleep(2000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.nullValue())
    }

    @Test
    fun createAlarm_RetrieveAlarm_CancelAlarm_CheckIfCancelled() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeAlarm(sourceIntent, 10000L)

        val currentRequestCode = requestCodeGenerator.getCurrentRequestCode()

        PendingIntent.getActivities(
            context,
            currentRequestCode,
            arrayOf(sourceIntent),
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val alarmIntent: PendingIntent? =
            PendingIntent.getActivities(
                context,
                currentRequestCode,
                arrayOf(sourceIntent),
                PendingIntent.FLAG_NO_CREATE
            )

        assertThat(alarmIntent, CoreMatchers.nullValue())
    }

}