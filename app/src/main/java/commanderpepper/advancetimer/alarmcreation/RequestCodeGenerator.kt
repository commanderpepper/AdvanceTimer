package commanderpepper.advancetimer.alarmcreation

import android.content.Context


object RequestCodeGenerator {

    private val REQUEST_CODE_KEY: String = "REQUEST_CODE_KEY"
    private val REQUEST_CODE: String = "REQUEST_CODE_FILE"
    private val DEFAULT_VALUE = 0

    fun getRequestCode(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(REQUEST_CODE, Context.MODE_PRIVATE)

        var requestCode = 0

        if (sharedPreferences.contains(REQUEST_CODE)) {
            requestCode = sharedPreferences.getInt(REQUEST_CODE_KEY, DEFAULT_VALUE) + 1
        } else {
            with(sharedPreferences.edit()) {
                putInt(REQUEST_CODE_KEY, DEFAULT_VALUE)
                commit()
            }
        }

        return requestCode
    }

}