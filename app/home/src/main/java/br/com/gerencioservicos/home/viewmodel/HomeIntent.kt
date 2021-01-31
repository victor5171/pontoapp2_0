package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType
import org.xtras.mvi.Intent

sealed class HomeIntent : Intent {
    object Load : HomeIntent()
    object AddWorklog : HomeIntent()
    data class ClickedOnPermission(val permissionType: PermissionType) : HomeIntent()
}
