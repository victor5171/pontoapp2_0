package org.xtras.tests.extensions.kotest

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.xtras.extensions.DispatchersContainer
import org.xtras.tests.extensions.dispatchers.FixedDispatchersContainer

public class CoroutineListener(
    @Suppress("MemberVisibilityCanBePrivate") public val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestListener {

    public val dispatchersContainer: DispatchersContainer = FixedDispatchersContainer(testCoroutineDispatcher)

    override suspend fun beforeContainer(testCase: TestCase) {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override suspend fun afterContainer(testCase: TestCase, result: TestResult) {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }
}
