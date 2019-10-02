package commanderpepper.advancetimer.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import commanderpepper.advancetimer.R
import commanderpepper.advancetimer.services.MyIntentService


class MainActivity : AppCompatActivity() {

    private lateinit var makeNotifcationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(commanderpepper.advancetimer.R.layout.activity_main)

        makeNotifcationButton = findViewById(R.id.create_button)

        makeNotifcationButton.setOnClickListener {
            val intent = Intent(this, MyIntentService::class.java)
            intent.putExtra("TESTA", "Woof")
            startService(intent)
        }
//        val intent = Intent(this, MyIntentService::class.java)
//        intent.putExtra("TEST", "Woof")
//        startService(intent)
    }
}
