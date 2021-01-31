package org.xtras.mvi

import org.xtras.mvi.transform.Transform
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

public class Reducer<TIntent, TState, TTransform>(
    private val coroutineScope: CoroutineScope,
    initialState: TState,
    private val logger: Logger? = null,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val intentExecutor: IntentExecutor<TIntent, TTransform>
) where TIntent : Intent,
        TState : State,
        TTransform : Transform<TState> {

    private val _transforms = MutableSharedFlow<TTransform>()
    private val multimap = Multimap<TIntent, Job>()

    public val state: StateFlow<TState> = _transforms
        .scan(initialState, this::reduce)
        .stateIn(coroutineScope, SharingStarted.Eagerly, initialState)

    public fun executeIntent(intent: TIntent) {
        logger?.logIntent(intent)

        val snapshotOfJobs = multimap.copy()
        val intentKiller = JobTerminatorImpl(snapshotOfJobs)

        val job = coroutineScope.launch {
            try {
                _transforms.emitAll(intentExecutor.executeIntent(intent, intentKiller))
            } catch (throwable: Throwable) {
                if(throwable !is TerminatedIntentException) {
                    logger?.logFailedIntent(intent, throwable)
                }
            }
        }

        val entry = multimap.put(intent, job)

        job.invokeOnCompletion { multimap.remove(entry) }
    }

    public inline fun <reified T : TState> requireState(): T = state.value as T

    @Suppress("FunctionName")
    private suspend fun reduce(previousState: TState, transform: TTransform): TState {
        return try {
            transform.reduce(previousState, defaultDispatcher).also { newState ->
                logger?.logTransformedNewState(transform, previousState, newState)
            }
        } catch (throwable: Throwable) {
            logger?.logFailedTransformNewState(transform, previousState, throwable)
            previousState
        }
    }
}
