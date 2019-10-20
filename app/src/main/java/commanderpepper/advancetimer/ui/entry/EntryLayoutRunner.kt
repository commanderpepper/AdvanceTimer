package commanderpepper.advancetimer.ui.entry

import android.view.View
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.ViewBinding
import commanderpepper.advancetimer.R

class EntryLayoutRunner(view: View): LayoutRunner<EntryWorkflow.Rendering> {
    override fun showRendering(rendering: EntryWorkflow.Rendering) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object : ViewBinding<EntryWorkflow.Rendering> by LayoutRunner.Companion.bind(
        R.layout.activity_entry, ::EntryLayoutRunner
    )
}