package org.xtras.tests.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.xtras.tests.extensions.kotest.CoroutineListener

private suspend fun delayFunction(): String {
    delay(1000)
    return "TEST"
}

internal class CoroutineListenerTests : BehaviorSpec({
    val coroutineListener = CoroutineListener()

    listener(coroutineListener)

    given("The replace on the listeners") {
        `when`("They are replaced") {
            then("All of the dispatchers inside the container should be a TestCoroutineScope dispatcher") {
                coroutineListener.testCoroutineDispatcher should beOfType(TestCoroutineDispatcher::class)
                coroutineListener.dispatchersContainer.Default shouldBe coroutineListener.testCoroutineDispatcher
                coroutineListener.dispatchersContainer.IO shouldBe coroutineListener.testCoroutineDispatcher
                coroutineListener.dispatchersContainer.Main shouldBe coroutineListener.testCoroutineDispatcher
                coroutineListener.dispatchersContainer.Unconfined shouldBe coroutineListener.testCoroutineDispatcher
            }
        }
    }

    given("A suspend function with delay") {
        `when`("I call it setting the coroutine listener") {
            coroutineListener.testCoroutineDispatcher.runBlockingTest {
                val returnedValue = delayFunction()

                then("The returned value should be TEST") {
                    returnedValue shouldBe "TEST"
                }
            }
        }
    }
})
