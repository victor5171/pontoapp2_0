package org.xtras.mvi

class StubPartialStateSender<TPartialState> : PartialStateSender<TPartialState> {
    private val sentPartialStates = mutableListOf<TPartialState>()

    override suspend fun send(partialState: TPartialState) {
        sentPartialStates.add(partialState)
    }

    fun sentPartialState(partialState: TPartialState) = sentPartialStates.contains(partialState)
}
