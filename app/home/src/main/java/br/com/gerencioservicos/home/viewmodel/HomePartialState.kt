package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import org.xtras.mvi.PartialState

internal sealed class HomePartialState : PartialState {
    data class ShowFullscreenError(val throwable: Throwable) : HomePartialState()
    data class ChangePermissionsAndWorklogs(val version: String, val permissionsAndWorklogs: PermissionsAndWorklogs) : HomePartialState()
    data class AskForPermission(val permissionType: PermissionType) : HomePartialState()
    data class ShowGenericError(val throwable: Throwable) : HomePartialState()
    data class ShowWarningAboutPermissions(val permissionTypes: Collection<PermissionType>) : HomePartialState()
    object OpenQrCodeScan : HomePartialState()
}
