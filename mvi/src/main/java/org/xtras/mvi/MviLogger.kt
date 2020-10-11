package org.xtras.mvi

interface MviLogger {
    fun logIntention(intention: Intention, currentState: State)
    fun logPartialState(intention: Intention, currentState: State, partialState: PartialState)
    fun logTransformedNewState(partialState: PartialState, previousState: State, newState: State)
    fun logFailedTransformNewState(partialState: PartialState, previousState: State, throwable: Throwable)
}
