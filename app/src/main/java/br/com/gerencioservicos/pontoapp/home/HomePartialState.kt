package br.com.gerencioservicos.pontoapp.home

import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.PartialState

internal sealed class HomePartialState : PartialState {
    data class ErrorCaught(val throwable: Throwable) : HomePartialState()
    data class ChangedPermissionsAndWorklogs(val permissionsAndWorklogs: PermissionsAndWorklogs) : HomePartialState()
}
