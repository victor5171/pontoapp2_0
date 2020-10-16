package org.xtras.mvi

public interface PartialStateSender<TPartialState> {
    suspend fun send(partialState: TPartialState)
}
