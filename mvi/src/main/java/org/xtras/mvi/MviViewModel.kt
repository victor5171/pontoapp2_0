package org.xtras.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class MviViewModel<TIntention : Intention, TState : State, TPartialState : PartialState>(
    initialState: TState,
    private val logger: MviLogger
) : ViewModel() {

    private val _partialStates = MutableSharedFlow<TPartialState>()

    @Suppress("LeakingThis")
    public val state: StateFlow<TState> = _partialStates
        .scan(initialState, this::_reduce)
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)

    public fun executeIntention(intention: TIntention) {
        logger.logIntention(intention, state.value)

        viewModelScope.launch {
            executeIntention(
                intention,
                state.value,
                object : PartialStateSender<TPartialState> {
                    override suspend fun send(partialState: TPartialState) {
                        logger.logPartialState(intention, state.value, partialState)

                        _partialStates.emit(partialState)
                    }
                }
            )
        }
    }

    protected abstract suspend fun executeIntention(intention: TIntention, currentState: TState, partialStateSender: PartialStateSender<TPartialState>)

    @Suppress("FunctionName")
    private fun _reduce(previousState: TState, partialState: TPartialState): TState {
        val newStateResult = tryReduce(previousState, partialState)

        val newState = newStateResult.getOrElse {
            logger.logFailedTransformNewState(partialState, previousState, it)
            return previousState
        }

        logger.logTransformedNewState(partialState, previousState, newState)
        return newState
    }

    protected abstract fun tryReduce(state: TState, partialState: TPartialState): Result<TState>
}
