package br.com.gerencioservicos.home.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.gerencioservicos.usecases.GetPendingPermissions
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
    private val getPendingPermissions: GetPendingPermissions,
    private val retrieveVersion: RetrieveVersion
) : MviViewModel<HomeIntention, HomeState, HomePartialState>(
    HomeState.Loading,
    StubMviLogger()
) {
    private val flowRetrier = FlowRetrier(
        viewModelScope,
        listenToPermissionsAndWorklogs().map {
            val version = retrieveVersion()
            HomePartialState.ChangePermissionsAndWorklogs(version, it)
        }
    ) {
        HomePartialState.ShowFullscreenError(it)
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
        HomeIntention.AddWorklog -> executeAddWorklog(partialStateSender)
    }

    private suspend fun executeClickedOnPermission(intention: HomeIntention.ClickedOnPermission, partialStateSender: PartialStateSender<HomePartialState>) {
        try {
            val isAllowed = isPermissionAllowed(intention.permissionType)
            if (!isAllowed) {
                partialStateSender.send(HomePartialState.AskForPermission(intention.permissionType))
            }
        } catch (exception: Exception) {
            partialStateSender.send(HomePartialState.ShowGenericError(exception))
        }
    }

    private suspend fun executeAddWorklog(partialStateSender: PartialStateSender<HomePartialState>) {
        try {
            val pendingPermissions = getPendingPermissions()

            if (pendingPermissions.isEmpty()) {
                partialStateSender.send(HomePartialState.OpenQrCodeScan)
                return
            }

            partialStateSender.send(HomePartialState.ShowWarningAboutPermissions(pendingPermissions))
        } catch (exception: Exception) {
            partialStateSender.send(HomePartialState.ShowGenericError(exception))
        }
    }

    override fun tryReduce(
        state: HomeState,
        partialState: HomePartialState
    ): Result<HomeState> = when (partialState) {
        is HomePartialState.ShowFullscreenError -> reduceErrorCaught(partialState)
        is HomePartialState.ChangePermissionsAndWorklogs -> reduceChangePermissionsAndWorklogs(state, partialState)
        is HomePartialState.AskForPermission -> reduceAskedForPermission(state, partialState)
        is HomePartialState.ShowGenericError -> reduceGenericErrorHappened(state, partialState)
        is HomePartialState.ShowWarningAboutPermissions -> reduceShowWarningAboutPermissions(state, partialState)
        HomePartialState.OpenQrCodeScan -> reduceOpenQrCodeScan(state)
    }

    private fun reduceErrorCaught(partialState: HomePartialState.ShowFullscreenError) = Result.success(HomeState.Error(partialState.throwable))

    private fun reduceChangePermissionsAndWorklogs(state: HomeState, partialState: HomePartialState.ChangePermissionsAndWorklogs): Result<HomeState> {
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

    private fun reduceAskedForPermission(state: HomeState, partialState: HomePartialState.AskForPermission) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(
            actions = actions.newInsertingActions(HomeAction.AskForPermission(partialState.permissionType))
        )
    }

    private fun reduceGenericErrorHappened(state: HomeState, partialState: HomePartialState.ShowGenericError) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(actionError = partialState.throwable)
    }

    private fun reduceShowWarningAboutPermissions(state: HomeState, partialState: HomePartialState.ShowWarningAboutPermissions) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(
            actions = actions.newInsertingActions(HomeAction.ShowWarningAboutPermissions(partialState.permissionTypes))
        )
    }

    private fun reduceOpenQrCodeScan(state: HomeState) = requireState<HomeState, HomeState.Loaded>(state) {
        copy(
            actions = actions.newInsertingActions(HomeAction.OpenQrCodeScan())
        )
    }
}
