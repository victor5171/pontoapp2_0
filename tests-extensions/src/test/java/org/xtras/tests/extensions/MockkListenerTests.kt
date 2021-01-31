package org.xtras.tests.extensions

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.matchers.shouldBe
import io.mockk.MockKException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import org.xtras.tests.extensions.kotest.MockkListener

private interface SimpleMockableInterface {
    val name: String
}

internal class MockkListenerTests : BehaviorSpec({
    // By purpose, forcing the isolation mode to reuse the same spec so mocks don't get disposed automatically
    isolationMode = IsolationMode.SingleInstance
    // By purpose, forcing the order to follow a sequential order
    testOrder = TestCaseOrder.Sequential
    listeners(MockkListener())
    given("A mocked interface which returns a name") {
        val mockedInterface = mockk<SimpleMockableInterface>()
        every { mockedInterface.name } returns "FirstName"
        `when`("I try to get the name") {
            then("This name should be returned") {
                mockedInterface.name shouldBe "FirstName"
            }
        }
        `when`("I try to use the mocked name again") {
            then("It should work") {
                mockedInterface.name shouldBe "FirstName"
            }
        }
    }
    given("A mocked interface which returns nothing") {
        val mockedInterface = mockk<SimpleMockableInterface>()
        `when`("I try to get the name") {
            then("It should throw an error") {
                assertThrows<MockKException> {
                    mockedInterface.name
                }
            }
        }
    }
})
