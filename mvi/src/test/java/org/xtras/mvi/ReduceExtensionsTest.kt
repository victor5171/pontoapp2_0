package org.xtras.mvi

import org.junit.Test
import kotlin.test.assertTrue

class ReduceExtensionsTest {

    private sealed class TestReduceStates : State {
        object State1 : TestReduceStates()
        object State2 : TestReduceStates()
        object State3 : TestReduceStates()
    }

    @Test
    fun `When I use requireState and my state is different than the required, it should return Result failure`() {
        val result = requireState<TestReduceStates.State2, TestReduceStates.State3>(TestReduceStates.State1) {
            TestReduceStates.State3
        }

        assertTrue { result.isFailure && result.exceptionOrNull()!! is StateRequiredNotFulfilledException }
    }

    @Test
    fun `When I use requireState and my state is equal to the required, it should return Result success`() {
        val result = requireState<TestReduceStates.State2, TestReduceStates.State3>(TestReduceStates.State2) {
            TestReduceStates.State3
        }

        assertTrue { result.isSuccess && result.getOrNull() == TestReduceStates.State3 }
    }
}
