package org.xtras.mvi.requirements

public interface ReduceRequirement<TState> {
    public fun reduce(state: TState): TState
}
