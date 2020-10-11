package br.com.gerencioservicos.pontoapp.home

import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.State
import org.xtras.mvi.actions.Action

internal sealed class HomeState : State {
    object Loading : HomeState()
    data class Error(val throwable: Throwable) : HomeState()
    data class Loaded(
        val permissionsAndWorklogs: PermissionsAndWorklogs,
        val actions: Iterable<Action>
    ) : HomeState()
}
