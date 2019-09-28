package commanderpepper.advancetimer.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import commanderpepper.advancetimer.services.MyIntentService


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(commanderpepper.advancetimer.R.layout.activity_main)

        val intent = Intent(this, MyIntentService::class.java)
        intent.putExtra("TEST", "Woof")
        startService(intent)
    }
}
