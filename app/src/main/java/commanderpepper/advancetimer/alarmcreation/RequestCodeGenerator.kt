package commanderpepper.advancetimer.alarmcreation

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting


class RequestCodeGenerator private constructor(val context: Context){

    private val REQUEST_CODE_KEY: String = "REQUEST_CODE_KEY"
    private val REQUEST_CODE_FILE: String = "requestCode"
    private val DEFAULT_VALUE = 0

    fun getRequestCode(): Int {
        val sharedPreferences =
            context.getSharedPreferences(REQUEST_CODE_FILE, Context.MODE_PRIVATE)

        var requestCode = sharedPreferences.getInt(REQUEST_CODE_KEY, DEFAULT_VALUE)
        requestCode++
        storeInt(sharedPreferences, requestCode)

        return requestCode
    }

    @VisibleForTesting
    fun getCurrentRequestCode(): Int {
        val sharedPreferences =
            context.getSharedPreferences(REQUEST_CODE_FILE, Context.MODE_PRIVATE)

        return sharedPreferences.getInt(REQUEST_CODE_KEY, DEFAULT_VALUE)
    }

    private fun storeInt(sharedPreferences: SharedPreferences, int: Int) {
        with(sharedPreferences.edit()) {
            putInt(REQUEST_CODE_KEY, int)
            apply()
        }
    }

    companion object{
        private var INSTANCE: RequestCodeGenerator? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = RequestCodeGenerator(context)
        }

        fun get(): RequestCodeGenerator {
            return INSTANCE ?: throw IllegalArgumentException("CrimeRepository must be initialized")
        }
    }
}