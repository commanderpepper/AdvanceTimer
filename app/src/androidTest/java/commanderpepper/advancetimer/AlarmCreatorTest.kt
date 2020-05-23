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
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


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
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        alarmCreator = AlarmCreator(context)
        requestCodeGenerator = (context as App).appComponent.requestCodeGenerator()
        requestCodeGenerator.clearSharedPreferences()
    }

    /**
     * Test the system's ability to create a pending intent and then retrieve it.
     */
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

    /**
     * Test the ability to create a pending, retrieve it and then check if equal.
     */
    @Test
    fun createPendingIntent_RetrievePendingIntent_CheckIfEqual() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        val retrievedIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        assertThat(alarmIntent, CoreMatchers.equalTo(retrievedIntent))

        try {
            alarmIntent.cancel()
            retrievedIntent.cancel()
        } finally {

        }
    }

    /**
     * Create two different pending intents and check that are not equal.
     */
    @Test
    fun createDifferentPendingIntent_CheckIfNotEqual() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val alarmIntent: PendingIntent =
            PendingIntent.getActivities(context, 0, arrayOf(sourceIntent), 0)

        val differentPendingIntent =
            PendingIntent.getActivities(context, 1, arrayOf(sourceIntent), 0)

        assertThat(alarmIntent, CoreMatchers.not(differentPendingIntent))

        try {
            alarmIntent.cancel()
        } finally {

        }
    }

    /**
     * Create nothing and check that pending intent retrieved is null.
     */
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

        try {
            alarmIntent?.cancel()
        } finally {

        }
    }

    /**
     * Test to see the request code generated within the alarm creator behaves as expected.
     */
    @Test
    fun createAlarmUsingContent_GetRequestCode_CheckIfOne() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeOneOffAlarm(requestCodeGenerator.getRequestCode(), 1, 200000L)

        val requestCode = requestCodeGenerator.getCurrentRequestCode()

        assertThat(requestCode, CoreMatchers.equalTo(1))
    }


    /**
     * Create a one off alarm using the alarm creator, get a pending intent with the same parameters and check not null.
     */
    @Test
    fun createAlarmUsingAlarmCreator_GetPendingIntent_CheckNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeOneOffAlarm(requestCodeGenerator.getRequestCode(), 1, 200000L)

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeGenerator.getCurrentRequestCode(),
            sourceIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        assertThat(alarmIntent, CoreMatchers.notNullValue())

        try {
            alarmIntent.cancel()
        } finally {

        }
    }

    /**
     * Create a repeating alarm using the alarm creator, get a pending intent with the same parameters and check not null.
     */
    @Test
    fun createRepeatingAlarmUsingAlarmCreator_GetPendingIntent_CheckNotNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeRepeatingAlarm(requestCodeGenerator.getRequestCode(), 1, 200000L, 200000L)

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeGenerator.getCurrentRequestCode(),
            sourceIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        assertThat(alarmIntent, CoreMatchers.notNullValue())

        try {
            alarmIntent.cancel()
        } finally {

        }
    }

    /**
     * Create a one off timer using the alarm creator, cancel the timer, check for null.
     */
    @Test
    fun createOneOffTimer_CancelTimer_GetTimer_CheckForNull() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)
        alarmCreator.makeOneOffAlarm(requestCodeGenerator.getRequestCode(), 1, 200000L)

        alarmCreator.cancelTimer(
            1,
            requestCodeGenerator.getCurrentRequestCode()
        )

        val testIntent = PendingIntent.getBroadcast(
            context,
            requestCodeGenerator.getCurrentRequestCode(),
            sourceIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        assertThat(testIntent, CoreMatchers.nullValue())
    }

    /**
     * Test to see what happens when a timer that doesn't exist is cancelled.
     * I see no errors when this test runs.
     */
    @Test
    fun createNoTimer_CancelNonExistentTimer_CheckForResult() {
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        alarmCreator.cancelTimer(0, 0)
    }

    @Test
    fun createTwoTimers_CheckIfBothExist(){
        val sourceIntent = Intent(context, AlarmReceiver::class.java)

        val requestCodeOne = requestCodeGenerator.getRequestCode()
        val requestCodeTwo = requestCodeGenerator.getRequestCode()

        alarmCreator.makeOneOffAlarm(requestCodeOne, 1, 2000000L)
        alarmCreator.makeOneOffAlarm(requestCodeTwo, 2, 2000000L)

        val alarmIntentOne: PendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeOne,
            sourceIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        val alarmIntentTwo: PendingIntent = PendingIntent.getBroadcast(
            context,
            requestCodeTwo,
            sourceIntent,
            PendingIntent.FLAG_NO_CREATE
        )

        assertThat(alarmIntentOne, CoreMatchers.notNullValue())
        assertThat(alarmIntentTwo, CoreMatchers.notNullValue())
    }

}