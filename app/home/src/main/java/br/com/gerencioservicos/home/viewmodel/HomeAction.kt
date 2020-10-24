package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType
import org.xtras.mvi.actions.Action

internal sealed class HomeAction : Action() {
    class AskForPermission(val permissionType: PermissionType) : HomeAction()
    class OpenQrCodeScan : HomeAction()
    class ShowWarningAboutPermissions(val permissionTypes: Collection<PermissionType>) : HomeAction()
}
