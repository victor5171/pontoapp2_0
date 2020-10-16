package br.com.gerencioservicos.pontoapp.home

import br.com.gerencioservicos.repository.permissions.PermissionType
import org.xtras.mvi.actions.Action

internal sealed class HomeAction : Action() {
    class AskForPermission(val permissionType: PermissionType) : HomeAction()
}
