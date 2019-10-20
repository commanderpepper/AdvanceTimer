package commanderpepper.advancetimer.ui.entry

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.parse
import javax.sql.StatementEvent

object EntryWorkflow :

/**
 * @param PropsT I don't think anything needs to be passed to the EntryWorkflow as
 * this is the main activity users will enter the app with. This may change in the future.
 * There are no parent workflows for the Entry Workflow, none that I can think of.
 *
 * @param State The internal state is that the entry activity
 */
    StatefulWorkflow<Unit, EntryWorkflow.State, Nothing, EntryWorkflow.Rendering>() {

    /**
     * Alarms = 1
     * Timers = 2
     * Options = 3
     */
    enum class State {
        Alarms, Timers, Options
    }

    data class Rendering(val item: String) {

    }

    override fun initialState(props: Unit, snapshot: Snapshot?) =
        snapshot?.bytes?.parse { source ->
            when (source.readInt()) {
                1 -> State.Alarms
                2 -> State.Timers
                else -> State.Options
            }
        } ?: State.Alarms

    override fun render(
        props: Unit,
        state: State,
        context: RenderContext<State, Nothing>
    ): Rendering {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun snapshotState(state: State): Snapshot {
        return when (state) {
            State.Alarms -> Snapshot.of(1)
            State.Timers -> Snapshot.of(2)
            else -> Snapshot.of(3)
        }
    }

}