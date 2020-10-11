package org.xtras.mvi

inline fun <reified TRequiredState, TFinalState> requireState(state: State, reduce: TRequiredState.() -> TFinalState): Result<TFinalState> {
    (state as? TRequiredState)?.let {
        val newState = reduce(it)
        return Result.success(newState)
    }

    return Result.failure(StateRequiredNotFulfilledException())
}
