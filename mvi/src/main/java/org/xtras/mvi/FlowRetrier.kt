package org.xtras.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

public class FlowRetrier<TPartialState>(
    private val coroutineScope: CoroutineScope,
    private val flow: Flow<TPartialState>,
    private val onError: (Throwable) -> TPartialState
) {
    private var collectionJob: Job? = null

    public fun cancel() {
        collectionJob?.cancel()
    }

    public fun retry(partialStateSender: PartialStateSender<TPartialState>) {
        cancel()

        collectionJob = coroutineScope.launch {
            flow.catch { emit(onError(it)) }.onEach { partialStateSender.send(it) }.collect()
        }
    }
}
