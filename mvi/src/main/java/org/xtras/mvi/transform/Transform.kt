package org.xtras.mvi.transform

import org.xtras.mvi.Loggable
import kotlinx.coroutines.CoroutineDispatcher

public interface Transform<TState> : Loggable {
    public suspend fun reduce(currentState: TState, defaultDispatcher: CoroutineDispatcher): TState {
        return this.reduce(currentState)
    }

    public fun reduce(currentState: TState): TState
}
