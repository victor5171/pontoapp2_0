package br.com.gerencioservicos.pontoapp.home

import androidx.lifecycle.viewModelScope
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import kotlinx.coroutines.flow.map
import org.xtras.mvi.FlowRetrier
import org.xtras.mvi.MviViewModel
import org.xtras.mvi.PartialStateSender
import org.xtras.mvi.StubMviLogger

internal class HomeViewModel(
    listenToPermissionsAndWorklogs: ListenToPermissionsAndWorklogs
) : MviViewModel<HomeIntention, HomeState, HomePartialState>(
    HomeState.Loading,
    StubMviLogger()
) {
    private val flowRetrier = FlowRetrier(
        viewModelScope,
        listenToPermissionsAndWorklogs().map { HomePartialState.ChangedPermissionsAndWorklogs(it) }
    ) {
        HomePartialState.ErrorCaught(it)
    }

    init {
        executeIntention(HomeIntention.Load)
    }

    override suspend fun executeIntention(
        intention: HomeIntention,
        currentState: HomeState,
        partialStateSender: PartialStateSender<HomePartialState>
    ) = when (intention) {
        HomeIntention.Load -> flowRetrier.retry(partialStateSender)
    }

    override fun tryReduce(
        state: HomeState,
        partialState: HomePartialState
    ): Result<HomeState> = when (partialState) {
        is HomePartialState.ErrorCaught -> reduceErrorCaught(partialState)
        is HomePartialState.ChangedPermissionsAndWorklogs -> reduceChangePermissionsAndWorklogs(state, partialState)
    }

    private fun reduceErrorCaught(partialState: HomePartialState.ErrorCaught) = Result.success(HomeState.Error(partialState.throwable))

    private fun reduceChangePermissionsAndWorklogs(state: HomeState, partialState: HomePartialState.ChangedPermissionsAndWorklogs): Result<HomeState> {
        if (state is HomeState.Loaded) {
            return Result.success(state.copy(permissionsAndWorklogs = partialState.permissionsAndWorklogs))
        }

        return Result.success(HomeState.Loaded(partialState.permissionsAndWorklogs, emptyList()))
    }
}
