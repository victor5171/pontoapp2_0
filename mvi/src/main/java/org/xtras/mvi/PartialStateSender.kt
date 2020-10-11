package org.xtras.mvi

interface PartialStateSender<TPartialState> {
    suspend fun send(partialState: TPartialState)
}
