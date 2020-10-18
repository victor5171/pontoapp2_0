package br.com.gerencioservicos.home.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.gerencioservicos.usecases.IsPermissionAllowed
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.RetrieveVersion
import kotlinx.coroutines.flow.map
import org.xtras.mvi.FlowRetrier
import org.xtras.mvi.MviViewModel
import org.xtras.mvi.PartialStateSender
import org.xtras.mvi.StubMviLogger
import org.xtras.mvi.actions.newInsertingActions
import org.xtras.mvi.requireState

internal class HomeViewModel(
    listenToPermissionsAndWorklogs: ListenToPermissionsAndWorklogs,
    private val isPermissionAllowed: IsPermissionAllowed,
    private val retrieveVersion: RetrieveVersion
) : MviViewModel<HomeIntention, HomeState, HomePartialState>(
    HomeState.Loading,
    StubMviLogger()
) {
    private val flowRetrier = FlowRetrier(
        viewModelScope,
        listenToPermissionsAndWorklogs().map {
            val version = retrieveVersion()
            HomePartialState.ChangedPermissionsAndWorklogs(version, it)
        }
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
        is HomeIntention.ClickedOnPermission -> executeClickedOnPermission(intention, partialStateSender)
    }

    private suspend fun executeClickedOnPermission(intention: HomeIntention.ClickedOnPermission, partialStateSender: PartialStateSender<HomePartialState>) {
        try {
            val isAllowed = isPermissionAllowed(intention.permissionType)
            if (!isAllowed) {
                partialStateSender.send(HomePartialState.AskedForPermission(intention.permissionType))
            }
        } catch (exception: Exception) {
            partialStateSender.send(HomePartialState.GenericErrorHappened(exception))
        }
    }

    override fun tryReduce(
        state: HomeState,
        partialState: HomePartialState
    ): Result<HomeState> = when (partialState) {
        is HomePartialState.ErrorCaught -> reduceErrorCaught(partialState)
        is HomePartialState.ChangedPermissionsAndWorklogs -> reduceChangePermissionsAndWorklogs(state, partialState)
        is HomePartialState.AskedForPermission -> reduceAskedForPermission(state, partialState)
        is HomePartialState.GenericErrorHappened -> reduceGenericErrorHappened(state, partialState)
    }

    private fun reduceErrorCaught(partialState: HomePartialState.ErrorCaught) = Result.success(HomeState.Error(partialState.throwable))

    private fun reduceChangePermissionsAndWorklogs(state: HomeState, partialState: HomePartialState.ChangedPermissionsAndWorklogs): Result<HomeState> {
        val homeListItems = buildList {
            partialState.permissionsAndWorklogs.permissions.forEach {
                add(HomeListItem.PermissionListItem(it))
            }

            if (partialState.permissionsAndWorklogs.numberOfPendingWorklogs > 0) {
                add(HomeListItem.PendingSynchronizationListItem(partialState.permissionsAndWorklogs.numberOfPendingWorklogs))
            }
        }

        if (state is HomeState.Loaded) {
            return Result.success(state.copy(homeListItems = homeListItems))
        }

        return Result.success(HomeState.Loaded(partialState.version, homeListItems, emptyList(), null))
    }

    private fun reduceAskedForPermission(state: HomeState, partialState: HomePartialState.AskedForPermission) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(actions = actions.newInsertingActions(HomeAction.AskForPermission(partialState.permissionType)))
    }

    private fun reduceGenericErrorHappened(state: HomeState, partialState: HomePartialState.GenericErrorHappened) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(actionError = partialState.throwable)
    }
}
