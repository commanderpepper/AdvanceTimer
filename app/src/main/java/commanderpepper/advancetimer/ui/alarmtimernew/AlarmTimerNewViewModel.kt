package commanderpepper.advancetimer.ui.alarmtimernew

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import commanderpepper.App
import commanderpepper.advancetimer.alarmcreation.AlarmCreator
import commanderpepper.advancetimer.repository.AlarmRepository
import commanderpepper.advancetimer.viewmodel.AlarmTimerViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class AlarmTimerNewViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmTimerViewModel: AlarmTimerViewModel =
        (application as App).appComponent.alarmTimerViewModelGenerator()

    fun makeTimerUsingContext(
        title: String,
        context: Context,
        triggerAtMillis: Long,
        parentId: Int?
    ) {
        alarmTimerViewModel.createTimerWaitForId(title, context, triggerAtMillis, parentId)
    }

    suspend fun createTimer(
        title: String,
        context: Context,
        triggerAtMillis: Long,
        parentId: Int?
    ): Flow<Int> {
        return alarmTimerViewModel.createTimer(title, context, triggerAtMillis, parentId)
    }
}
