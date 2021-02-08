package commanderpepper.advancetimer.workmanager

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import commanderpepper.advancetimer.receivers.DISMISS_TIMER_ID
import commanderpepper.advancetimer.ui.dismisstimer.DismissTimer

class AlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val intent = Intent(applicationContext, DismissTimer::class.java)
        val timerId = inputData.getInt(DISMISS_TIMER_ID, -1)

        if (timerId == -1) return Result.failure()

        intent.putExtra(DISMISS_TIMER_ID, timerId)
        Handler(Looper.getMainLooper()).post {
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        return Result.success()
    }
}