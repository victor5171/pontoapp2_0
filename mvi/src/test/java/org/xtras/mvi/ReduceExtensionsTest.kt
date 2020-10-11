package org.xtras.mvi

import org.junit.Test
import kotlin.test.assertTrue

class ReduceExtensionsTest {

    private sealed class TestReduceStates : State {
        object State1 : TestReduceStates()
        object State2 : TestReduceStates()
        object State3 : TestReduceStates()
        object State4 : TestReduceStates()
    }

    @Test
    fun `When I use requireState and my state is different than the required, it should return Result failure`() {
        val result = requireState<TestReduceStates, TestReduceStates.State2>(TestReduceStates.State1) {
            TestReduceStates.State3
        }

        assertTrue { result.isFailure && result.exceptionOrNull()!! is StateRequiredNotFulfilledException }
    }

    @Test
    fun `When I use requireState and my state is equal to the required, it should return Result success`() {
        val result = requireState<TestReduceStates, TestReduceStates.State2>(TestReduceStates.State2) {
            TestReduceStates.State3
        }

        assertTrue { result.isSuccess && result.getOrNull() == TestReduceStates.State3 }
    }

    @Test
    fun `When I use multiple ors for required states and only the last one works, it should return the success from the last one`() {
        val result = requireState<TestReduceStates, TestReduceStates.State1>(TestReduceStates.State3) {
            TestReduceStates.State1
        } or requireState<TestReduceStates, TestReduceStates.State2>(TestReduceStates.State3) {
            TestReduceStates.State2
        } or requireState<TestReduceStates, TestReduceStates.State3>(TestReduceStates.State3) {
            TestReduceStates.State3
        }

        assertTrue { result.isSuccess && result.getOrNull() == TestReduceStates.State3 }
    }

    @Test
    fun `When I use multiple ors for required states and no one works, it should return Result failure`() {
        val result = requireState<TestReduceStates, TestReduceStates.State1>(TestReduceStates.State4) {
            TestReduceStates.State1
        } or requireState<TestReduceStates, TestReduceStates.State2>(TestReduceStates.State4) {
            TestReduceStates.State2
        } or requireState<TestReduceStates, TestReduceStates.State3>(TestReduceStates.State4) {
            TestReduceStates.State3
        }

        assertTrue { result.isFailure && result.exceptionOrNull()!! is StateRequiredNotFulfilledException }
    }
}
