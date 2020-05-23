package commanderpepper.advancetimer.ui.alarmtimerdetail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import timber.log.Timber
import java.lang.ClassCastException
import java.lang.IllegalStateException

class DeleteDialog : DialogFragment() {

    private lateinit var listener: DeleteDialogListener

    interface DeleteDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = targetFragment as DeleteDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString()) + "must implement DeleteDialogInterface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)

            builder.setTitle("Warning!")
            builder.setMessage("Deleting this timer will also delete all of its children timers.")

            builder.setPositiveButton(
                "Delete"
            ) { _, _ ->
                Timber.d("Delete clicked")
                listener.onDialogPositiveClick(this)
            }

            builder.setNegativeButton(
                "Cancel"
            ) { _, _ ->
                Timber.d("Cancel clicked")
                listener.onDialogNegativeClick(this)
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null ")
    }
}