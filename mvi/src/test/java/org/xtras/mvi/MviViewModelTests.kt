package org.xtras.mvi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.ClassCastException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MviViewModelTests {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @ExperimentalCoroutinesApi
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When I start my test view model, it should have a loading state`() {
        val testMviViewModel = TestMviViewModel(StubMviLogger())

        testMviViewModel.state.observeForever {}

        assertEquals(TestState.Loading, testMviViewModel.state.value)
    }

    @Test
    fun `When I make it ready and then post a add name intention, it should log correctly the intention and produce a new state`() {
        val stubMviLogger = StubMviLogger()

        val testMviViewModel = TestMviViewModel(stubMviLogger)

        val makeReadyIntention = TestIntention.MakeReady
        testMviViewModel.executeIntention(makeReadyIntention)

        val value = testMviViewModel.state.blockingObserve()

        assertTrue { (value as TestState.Loaded).names.isEmpty() }

        assertTrue {
            val state = stubMviLogger.getLoggedIntention(makeReadyIntention)
            state is TestState.Loading
        }

        assertTrue {
            val stateAndPartialState = stubMviLogger.getLoggedStateAndPartialState(makeReadyIntention)
            requireNotNull(stateAndPartialState)

            val (state, partialState) = stateAndPartialState

            require(state is TestState.Loading)

            require(partialState is TestPartialState.MadeReady)

            val stateTransformationLog = stubMviLogger.getLoggedTransformedStates(partialState)
            requireNotNull(stateTransformationLog)

            val (previousState, newState) = stateTransformationLog

            previousState is TestState.Loading &&
                (newState as TestState.Loaded).names.isEmpty()
        }

        val addNamesIntention = TestIntention.AddNames("Name")
        testMviViewModel.executeIntention(addNamesIntention)

        val valueAfterAddNames = testMviViewModel.state.blockingObserve()

        assertTrue { (valueAfterAddNames as TestState.Loaded).names.contains("Name") }

        assertTrue {
            val state = stubMviLogger.getLoggedIntention(addNamesIntention)
            state is TestState.Loaded
        }

        assertTrue {
            val stateAndPartialState = stubMviLogger.getLoggedStateAndPartialState(addNamesIntention)
            requireNotNull(stateAndPartialState)

            val (state, partialState) = stateAndPartialState

            require(state is TestState.Loaded)

            require((partialState as TestPartialState.AddedName).name == "Name")

            val stateTransformationLog = stubMviLogger.getLoggedTransformedStates(partialState)
            requireNotNull(stateTransformationLog)

            val (previousState, newState) = stateTransformationLog

            (previousState as TestState.Loaded).names.isEmpty() &&
                (newState as TestState.Loaded).names.contains("Name")
        }
    }

    @Test
    fun `When I try to add names, but the screen is still loading, it should log an error for unexpected state and stay in the previous state`() {
        val stubMviLogger = StubMviLogger()

        val testMviViewModel = TestMviViewModel(stubMviLogger)

        val addNamesIntention = TestIntention.AddNames("Name")
        testMviViewModel.executeIntention(addNamesIntention)

        val value = testMviViewModel.state.blockingObserve()

        assertTrue { value is TestState.Loading }

        val stateAndPartialState = stubMviLogger.getLoggedStateAndPartialState(addNamesIntention)
        requireNotNull(stateAndPartialState)

        val (_, partialState) = stateAndPartialState

        assertTrue { stubMviLogger.getLoggedTransformedStates(partialState) == null }

        assertTrue {
            val stateFailedTransformationLog = stubMviLogger.getLoggedFailedStateTransformation(partialState)
            requireNotNull(stateFailedTransformationLog)

            val (oldState, exception) = stateFailedTransformationLog

            oldState is TestState.Loading && exception is StateRequiredNotFulfilledException
        }
    }

    @Test
    fun `When I try to add a name and then submit it, it should submit it correctly`() {
        val stubMviLogger = StubMviLogger()

        val testMviViewModel = TestMviViewModel(stubMviLogger)

        val emissions = mutableListOf<TestState>()
        testMviViewModel.state.observeForever {
            emissions.add(it)
        }

        testMviViewModel.executeIntention(TestIntention.MakeReady)

        testMviViewModel.executeIntention(TestIntention.AddNames("Name 1"))
        testMviViewModel.executeIntention(TestIntention.AddNames("Name 2"))
        testMviViewModel.executeIntention(TestIntention.SubmitNames)

        val lastEmission = emissions.last()

        assertTrue {
            val names = (lastEmission as TestState.NamesSubmitted).names
            names.containsAll(listOf("Name 1", "Name 2"))
        }
    }

    @Test
    fun `When I try to submit names, but the state is not on Loaded, it should change to error state`() {
        val stubMviLogger = StubMviLogger()

        val testMviViewModel = TestMviViewModel(stubMviLogger)

        testMviViewModel.executeIntention(TestIntention.SubmitNames)

        val value = testMviViewModel.state.blockingObserve()

        assertTrue {
            value is TestState.Error && value.throwable is ClassCastException
        }
    }
}
