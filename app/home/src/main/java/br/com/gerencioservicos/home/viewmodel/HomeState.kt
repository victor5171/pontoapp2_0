package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.State

internal sealed class HomeState : State {
    object Loading : HomeState()
    data class Error(val throwable: Throwable) : HomeState()
    data class Loaded(
        val permissionsAndWorklogs: PermissionsAndWorklogs,
        val actions: Iterable<HomeAction>,
        val actionError: Throwable?
    ) : HomeState()
}
