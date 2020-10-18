package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.PartialState

internal sealed class HomePartialState : PartialState {
    data class ErrorCaught(val throwable: Throwable) : HomePartialState()
    data class ChangedPermissionsAndWorklogs(val version: String, val permissionsAndWorklogs: PermissionsAndWorklogs) : HomePartialState()
    data class AskedForPermission(val permissionType: PermissionType) : HomePartialState()
    data class GenericErrorHappened(val throwable: Throwable) : HomePartialState()
}
