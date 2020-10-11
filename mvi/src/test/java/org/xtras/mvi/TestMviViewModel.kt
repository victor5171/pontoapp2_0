package org.xtras.mvi

sealed class TestIntention : Intention {
    object MakeReady : TestIntention()
    data class AddNames(val name: String) : TestIntention()
    object SubmitNames : TestIntention()
}

sealed class TestState : State {
    object Loading : TestState()
    data class Loaded(val names: List<String>) : TestState()
    data class NamesSubmitted(val names: List<String>) : TestState()
    data class Error(val throwable: Throwable) : TestState()
}

sealed class TestPartialState : PartialState {
    object MadeReady : TestPartialState()
    data class AddedName(val name: String) : TestPartialState()
    data class NamesSubmitted(val names: List<String>) : TestPartialState()
    data class NamesSubmittedFailed(val error: Throwable) : TestPartialState()
}

class TestMviViewModel(mviLogger: MviLogger) : MviViewModel<TestIntention, TestState, TestPartialState>(
    TestState.Loading,
    mviLogger
) {
    override suspend fun executeIntention(intention: TestIntention, currentState: TestState, partialStateSender: PartialStateSender<TestPartialState>) = when (intention) {
        TestIntention.MakeReady -> makeReady(partialStateSender)
        is TestIntention.AddNames -> addNames(intention, partialStateSender)
        TestIntention.SubmitNames -> submitNames(currentState, partialStateSender)
    }

    private suspend fun makeReady(partialStateSender: PartialStateSender<TestPartialState>) {
        partialStateSender.send(TestPartialState.MadeReady)
    }

    private suspend fun addNames(intention: TestIntention.AddNames, partialStateSender: PartialStateSender<TestPartialState>) {
        partialStateSender.send(TestPartialState.AddedName(intention.name))
    }

    private suspend fun submitNames(currentState: TestState, partialStateSender: PartialStateSender<TestPartialState>) {
        try {
            val currentNames = (currentState as TestState.Loaded).names
            partialStateSender.send(TestPartialState.NamesSubmitted(currentNames))
        } catch (throwable: Throwable) {
            partialStateSender.send(TestPartialState.NamesSubmittedFailed(throwable))
        }
    }

    override fun tryReduce(state: TestState, partialState: TestPartialState): Result<TestState> {
        return when (partialState) {
            is TestPartialState.MadeReady -> reduceMadeReady()
            is TestPartialState.AddedName -> reduceAddedName(state, partialState)
            is TestPartialState.NamesSubmitted -> reduceNamesSubmitted(state, partialState)
            is TestPartialState.NamesSubmittedFailed -> reduceNamesSubmittedFailed(partialState)
        }
    }

    private fun reduceMadeReady() = Result.success(TestState.Loaded(emptyList()))

    private fun reduceAddedName(state: TestState, partialState: TestPartialState.AddedName) = requireState<TestState.Loaded, TestState.Loaded>(state) {
        val newList = names.toMutableList()
        newList.add(partialState.name)

        copy(
            names = newList
        )
    }

    private fun reduceNamesSubmitted(state: TestState, partialState: TestPartialState.NamesSubmitted) = requireState<TestState.Loaded, TestState.NamesSubmitted>(state) {
        TestState.NamesSubmitted(partialState.names)
    }

    private fun reduceNamesSubmittedFailed(partialState: TestPartialState.NamesSubmittedFailed) = Result.success(TestState.Error(partialState.error))
}
