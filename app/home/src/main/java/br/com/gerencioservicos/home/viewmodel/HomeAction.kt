package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType

internal sealed class HomeAction {
    class AskForPermission(val permissionType: PermissionType) : HomeAction()
    object OpenQrCodeScan : HomeAction()
    class ShowWarningAboutPermissions(val permissionTypes: Collection<PermissionType>) : HomeAction()
    class ShowGenericError(val throwable: Throwable) : HomeAction()
}
