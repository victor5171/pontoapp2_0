package org.xtras.tests.extensions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.junit.jupiter.api.assertThrows
import org.xtras.tests.extensions.kotest.matchers.beOfState
import org.xtras.tests.extensions.kotest.matchers.shouldBeOfState

private sealed class StateMatcherStates {
    data class State1(val number: Int) : StateMatcherStates()
    object State2 : StateMatcherStates()
}

internal class StateMatcherTests : BehaviorSpec({
    given("A state of value State1") {
        val state: StateMatcherStates = StateMatcherStates.State1(1)

        `when`("I try to use shouldBeOfState directly, checking if it's State1") {
            then("It should pass") {
                state.shouldBeOfState<StateMatcherStates.State1>()
            }
        }

        `when`("I try to use shouldBeOfState directly, checking if it's State1 but the internal assertions fail") {
            then("It should fail") {
                assertThrows<AssertionError> {
                    state.shouldBeOfState { state1: StateMatcherStates.State1 ->
                        state1.number.shouldBe(0)
                    }
                }
            }
        }

        `when`("I try to use shouldNot in conjunction with beOfState, checking if it's not State1") {
            then("It should fail") {
                assertThrows<AssertionError> {
                    state shouldNot beOfState<StateMatcherStates.State1>()
                }
            }
        }

        `when`("I try to use shouldBeOfState directly, checking if it's State2 with internal assertions") {
            then("It should fail") {
                var internalAssertionsCalled = false
                assertThrows<AssertionError> {
                    state.shouldBeOfState { _: StateMatcherStates.State2 ->
                        internalAssertionsCalled = true
                    }
                }

                internalAssertionsCalled.shouldBeFalse()
            }
        }
    }
})
