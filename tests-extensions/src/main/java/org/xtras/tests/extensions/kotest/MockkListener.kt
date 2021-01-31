package org.xtras.tests.extensions.kotest

import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.mockk.clearAllMocks

public class MockkListener : TestListener {
    override suspend fun beforeContainer(testCase: TestCase) {
        if (testCase.description.isRootTest()) {
            clearAllMocks()
        }
    }
}
