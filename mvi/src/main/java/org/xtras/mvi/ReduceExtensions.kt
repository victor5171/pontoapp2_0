package org.xtras.mvi

inline fun <TState, reified TRequiredState> requireState(state: TState, reduce: TRequiredState.() -> TState): Result<TState> {
    (state as? TRequiredState)?.let {
        val newState = reduce(it)
        return Result.success(newState)
    }

    return Result.failure(StateRequiredNotFulfilledException())
}

infix fun <TState> Result<TState>.or(another: Result<TState>): Result<TState> {
    if (this.isSuccess) {
        return this
    }

    if (another.isSuccess) {
        return another
    }

    return Result.failure(StateRequiredNotFulfilledException())
}
