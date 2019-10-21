package commanderpepper.advancetimer.ui.alarmlist

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.StatelessWorkflow

object AlarmListWorkflow : StatefulWorkflow<Unit, AlarmListWorkflow.State, Nothing, AlarmListWorkflow.Rendering>() {

    enum class State {
        SubMenuOpen,
        SubMenuNotOpen
    }

    data class Rendering(val list : List<String>){

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