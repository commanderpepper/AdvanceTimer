package commanderpepper.advancetimer.ui.alarmtimerdetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import timber.log.Timber
import java.lang.IllegalStateException

class DeleteDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
        return activity?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)

            builder.setTitle("Warning!")
            builder.setMessage("Deleting this timer will also delete all of its child timers.")

            builder.setPositiveButton("Delete",
                DialogInterface.OnClickListener { dialog, which ->
                    Timber.d("Delete clicked")
                })

            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    Timber.d("Cancel clicked")
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null ")
    }
}