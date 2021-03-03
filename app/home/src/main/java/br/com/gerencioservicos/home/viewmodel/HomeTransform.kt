package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.Actions
import org.xtras.mvi.requirements.requireAndReduceState
import org.xtras.mvi.transform.Transform

internal sealed class HomeTransform : Transform<HomeState> {

    data class ShowFullscreenError(private val throwable: Throwable) : HomeTransform() {
        override fun reduce(currentState: HomeState): HomeState {
            return HomeState.Error(throwable)
        }
    }

    data class ChangePermissionsAndWorklogs(private val version: String, private val permissionsAndWorklogs: PermissionsAndWorklogs) : HomeTransform() {
        override fun reduce(currentState: HomeState): HomeState {
            val homeListItems = buildList {
                permissionsAndWorklogs.permissions.forEach {
                    add(HomeListItem.PermissionListItem(it))
                }

                if (permissionsAndWorklogs.numberOfPendingWorklogs > 0) {
                    add(HomeListItem.PendingSynchronizationListItem(permissionsAndWorklogs.numberOfPendingWorklogs))
                }
            }

            if (currentState is HomeState.Loaded) {
                return currentState.copy(homeListItems = homeListItems)
            }

            return HomeState.Loaded(version, homeListItems, Actions(), null)
        }
    }

    data class AddAction(private val homeAction: HomeAction) : HomeTransform() {
        override fun reduce(currentState: HomeState) = requireAndReduceState(currentState) { loadedState: HomeState.Loaded ->
            loadedState.copy(actions = loadedState.actions.add(homeAction))
        }
    }
}
