package org.xtras.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

abstract class MviViewModel<TIntention : Intention, TState : State, TPartialState : PartialState>(
    initialState: TState,
    private val logger: MviLogger
) : ViewModel() {

    private var currentState: TState = initialState

    private val partialStatesChannel = Channel<TPartialState>(Channel.UNLIMITED)

    @Suppress("LeakingThis")
    val state = partialStatesChannel.consumeAsFlow()
        .scan(initialState, this::_reduce)
        .onEach { currentState = it }
        .asLiveData()

    fun executeIntention(intention: TIntention) {
        logger.logIntention(intention, currentState)

        viewModelScope.launch {
            executeIntention(
                intention,
                currentState,
                object : PartialStateSender<TPartialState> {
                    override suspend fun send(partialState: TPartialState) {
                        logger.logPartialState(intention, currentState, partialState)

                        partialStatesChannel.send(partialState)
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
