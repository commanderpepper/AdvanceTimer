package commanderpepper.advancetimer.ui.entry

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow

object EntryWorkflow :
    StatefulWorkflow<Unit, EntryWorkflow.State, Nothing, EntryWorkflow.Rendering>() {

    enum class State {
        Alarms, Timers, Options
    }

    data class Rendering(val item: String) {

    }

    override fun initialState(props: Unit, snapshot: Snapshot?): State {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(
        props: Unit,
        state: State,
        context: RenderContext<State, Nothing>
    ): Rendering {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun snapshotState(state: State): Snapshot {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}