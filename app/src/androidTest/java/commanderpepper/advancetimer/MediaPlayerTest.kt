package commanderpepper.advancetimer

import android.media.MediaPlayer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaPlayerTest {

    private lateinit var mediaPlayer: MediaPlayer

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        mediaPlayer = MediaPlayer.create(context, R.raw.bell_ringing_04)
    }

    @After
    fun cleanUp() {
        mediaPlayer.stop()
    }

    @Test
    fun doNothing_CheckNotPlaying(){
        assertFalse(mediaPlayer.isPlaying)
    }

    @Test
    fun playSound_CheckIfPlaying() {
        mediaPlayer.start()
        assertTrue(mediaPlayer.isPlaying)
    }

    @Test
    fun playSound_Stop_CheckIfStopped() {
        mediaPlayer.start()
        assertTrue(mediaPlayer.isPlaying)

        mediaPlayer.stop()
        assertTrue(!mediaPlayer.isPlaying)
    }

    @Test
    fun playSoundRepeatedly_CheckIfPlaying() {
        mediaPlayer.start()
        assertTrue(mediaPlayer.isPlaying)

        mediaPlayer.isLooping = true
        assertTrue(mediaPlayer.isLooping)
        assertTrue(mediaPlayer.isPlaying)
    }
}