package org.xtras.tests.extensions.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.xtras.extensions.DispatchersContainer

/**
 * Use this DispatcherContainer only in tests
 */
public object UnconfinedTestsDispatcherContainer : DispatchersContainer {
    override val Default: CoroutineDispatcher = Dispatchers.Unconfined
    override val Main: CoroutineDispatcher = Dispatchers.Unconfined
    override val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher = Dispatchers.Unconfined
}
