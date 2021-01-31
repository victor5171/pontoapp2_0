package org.xtras.mvi.reducer

import org.xtras.mvi.Intent
import org.xtras.mvi.IntentExecutor
import org.xtras.mvi.Logger
import org.xtras.mvi.Reducer
import org.xtras.mvi.State
import org.xtras.mvi.transform.Transform
import io.kotest.assertions.throwables.shouldThrow
import org.xtras.mvi.TerminatedIntentException
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineScope
import org.xtras.tests.extensions.kotest.CoroutineListener

private sealed class TestIntent : Intent {
    object SimpleIntent : TestIntent()

    object Transform1Producer : TestIntent()

    object FailedTransformProducer : TestIntent()
}

private sealed class TestState : State {
    object InitialState : TestState()

    object StateFromTransform1 : TestState()
}

private sealed class TestTransform : Transform<TestState> {
    object Transform1 : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            return TestState.StateFromTransform1
        }
    }

    object FailedTransform : TestTransform() {
        override fun reduce(currentState: TestState): TestState {
            throw Exception()
        }
    }
}

internal class ReducerTests : BehaviorSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val intentExecutor = mockk<IntentExecutor<TestIntent, TestTransform>>()
    val logger = mockk<Logger>(relaxUnitFun = true)

    val coroutineListener = CoroutineListener()
    listeners(coroutineListener)

    fun createReducer() = Reducer(
        coroutineScope = TestCoroutineScope(),
        initialState = TestState.InitialState,
        logger = logger,
        intentExecutor = intentExecutor
    )

    given("A reducer with a initial state") {
        val reducer = createReducer()

        `when`("I listen to its first value") {
            val firstValue = reducer.state.value

            then("It should be the initial state") {
                firstValue shouldBe TestState.InitialState
            }
        }

        `when`("I try to require a state different than the current state") {
            then("It should cause a ClassCastException") {
                shouldThrow<ClassCastException> {
                    reducer.requireState<TestState.StateFromTransform1>()
                }
            }
        }

        `when`("I try to require a state equal to the current state") {
            val requiredState = reducer.requireState<TestState.InitialState>()

            then("It should return the cast state") {
                requiredState shouldBe TestState.InitialState
            }
        }

        `when`("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            then("This intent should be logged") {
                verify(exactly = 1) { logger.logIntent(TestIntent.SimpleIntent) }
                verify(exactly = 1) { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) }
            }
        }
    }

    given("A reducer which triggers a TerminatedIntentException when executing a SimpleIntent") {
        val exceptionToThrow = TerminatedIntentException()
        every { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) } throws exceptionToThrow
        val reducer = createReducer()

        `when`("I execute an intent and it is cancelled throwing TerminatedIntentException") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            then("The exception should NOT be logged") {
                verify(exactly = 0) { logger.logFailedIntent(TestIntent.SimpleIntent, exceptionToThrow) }
            }
        }
    }

    given("A reducer which fails to execute a SimpleIntent") {
        val reducer = createReducer()

        val exceptionToThrow = Exception()
        every { intentExecutor.executeIntent(TestIntent.SimpleIntent, any()) } throws exceptionToThrow

        `when`("I execute an intent") {
            reducer.executeIntent(TestIntent.SimpleIntent)

            then("The exception should be logged") {
                verify(exactly = 1) { logger.logFailedIntent(TestIntent.SimpleIntent, exceptionToThrow) }
            }
        }
    }

    given("A reducer which produces Transform1 partial state when Transform1Producer intent is sent") {
        val reducer = createReducer()

        every {
            intentExecutor.executeIntent(
                TestIntent.Transform1Producer,
                any()
            )
        } returns flowOf(TestTransform.Transform1)

        `when`("I execute Transform1Producer intent") {
            reducer.executeIntent(TestIntent.Transform1Producer)

            then("The state should change to the state which Transform1 produces") {
                reducer.state.value shouldBe TestState.StateFromTransform1
                verify(exactly = 1) {
                    logger.logTransformedNewState(
                        transform = TestTransform.Transform1,
                        previousState = TestState.InitialState,
                        newState = TestState.StateFromTransform1
                    )
                }
            }
        }
    }

    given("A reducer which produces FailedTransform partial state when FailedTransformProducer intent is sent") {
        val reducer = createReducer()

        every {
            intentExecutor.executeIntent(
                TestIntent.FailedTransformProducer,
                any()
            )
        } returns flowOf(TestTransform.FailedTransform)

        `when`("I execute an FailedTransformProducer intent") {
            reducer.executeIntent(TestIntent.FailedTransformProducer)

            then("The current state should remain the same") {
                reducer.state.value shouldBe TestState.InitialState
            }

            then("The failure should be logged") {
                verify(exactly = 1) {
                    logger.logFailedTransformNewState(
                        TestTransform.FailedTransform,
                        TestState.InitialState,
                        ofType<Exception>()
                    )
                }
            }
        }
    }
})
