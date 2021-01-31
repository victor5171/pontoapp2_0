package org.xtras.mvi.requirements

import org.xtras.mvi.State
import org.xtras.mvi.StateRequiredNotFulfilledException
import kotlin.reflect.KClass
import kotlin.reflect.cast

public class StateReduceRequirement<TState, TRequiredState>(
    private val expectedState: KClass<out TRequiredState>,
    private val reduceFunction: (TRequiredState) -> TState
) : ReduceRequirement<TState> where TState : State, TRequiredState : TState {

    public override fun reduce(state: TState): TState {
        if (expectedState.isInstance(state)) {
            return reduceFunction(expectedState.cast(state))
        }

        throw StateRequiredNotFulfilledException(expectedState = expectedState, realState = state::class)
    }
}
