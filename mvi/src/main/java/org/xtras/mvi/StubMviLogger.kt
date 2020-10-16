package org.xtras.mvi

public class StubMviLogger : MviLogger {
    private val loggedIntentions = mutableMapOf<Intention, State>()
    private val loggedPartialStates = mutableMapOf<Intention, Pair<State, PartialState>>()
    private val loggedTransformedStates = mutableMapOf<PartialState, Pair<State, State>>()
    private val loggedFailedStateTransformations = mutableMapOf<PartialState, Pair<State, Throwable>>()

    override fun logIntention(intention: Intention, currentState: State) {
        loggedIntentions[intention] = currentState
    }

    override fun logPartialState(intention: Intention, currentState: State, partialState: PartialState) {
        loggedPartialStates[intention] = currentState to partialState
    }

    override fun logTransformedNewState(
        partialState: PartialState,
        previousState: State,
        newState: State
    ) {
        loggedTransformedStates[partialState] = previousState to newState
    }

    override fun logFailedTransformNewState(
        partialState: PartialState,
        previousState: State,
        throwable: Throwable
    ) {
        loggedFailedStateTransformations[partialState] = previousState to throwable
    }

    public fun getLoggedIntention(intention: Intention) = loggedIntentions[intention]

    public fun getLoggedStateAndPartialState(intention: Intention) = loggedPartialStates[intention]

    public fun getLoggedTransformedStates(partialState: PartialState) = loggedTransformedStates[partialState]

    public fun getLoggedFailedStateTransformation(partialState: PartialState) = loggedFailedStateTransformations[partialState]
}
