package br.com.gerencioservicos.home.viewmodel

import org.xtras.mvi.Actions
import org.xtras.mvi.State

internal sealed class HomeState : State {
    object Loading : HomeState()
    data class Error(val throwable: Throwable) : HomeState()
    data class Loaded(
        val version: String,
        val homeListItems: List<HomeListItem>,
        val actions: Actions<HomeAction>
    ) : HomeState()
}
