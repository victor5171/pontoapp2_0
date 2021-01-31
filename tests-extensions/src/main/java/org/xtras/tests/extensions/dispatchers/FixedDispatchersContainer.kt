package org.xtras.tests.extensions.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import org.xtras.extensions.DispatchersContainer

public class FixedDispatchersContainer(
    coroutineDispatcher: CoroutineDispatcher
) : DispatchersContainer {
    override val Default: CoroutineDispatcher = coroutineDispatcher
    override val Main: CoroutineDispatcher = coroutineDispatcher
    override val Unconfined: CoroutineDispatcher = coroutineDispatcher
    override val IO: CoroutineDispatcher = coroutineDispatcher
}
