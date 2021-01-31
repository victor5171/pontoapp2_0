@file:Suppress("PropertyName")

package org.xtras.extensions

import kotlinx.coroutines.CoroutineDispatcher

public interface DispatchersContainer {
    public val Default: CoroutineDispatcher
    public val Main: CoroutineDispatcher
    public val Unconfined: CoroutineDispatcher
    public val IO: CoroutineDispatcher
}
