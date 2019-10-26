package commanderpepper.advancetimer.alarmcreation

import android.content.Context
import android.content.SharedPreferences


object RequestCodeGenerator {

    private val REQUEST_CODE_KEY: String = "REQUEST_CODE_KEY"
    private val REQUEST_CODE_FILE: String = "requestCode"
    private val DEFAULT_VALUE = 0

    fun getRequestCode(context: Context): Int {
        val sharedPreferences =
            context.getSharedPreferences(REQUEST_CODE_FILE, Context.MODE_PRIVATE)

        var requestCode = sharedPreferences.getInt(REQUEST_CODE_KEY, DEFAULT_VALUE)
        requestCode++
        storeInt(sharedPreferences, requestCode)

        return requestCode
    }

    private fun storeInt(sharedPreferences: SharedPreferences, int: Int) {
        with(sharedPreferences.edit()) {
            putInt(REQUEST_CODE_KEY, int)
            apply()
        }
    }

}