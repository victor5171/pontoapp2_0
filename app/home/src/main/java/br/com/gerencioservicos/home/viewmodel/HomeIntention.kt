package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.PermissionType
import org.xtras.mvi.Intention

sealed class HomeIntention : Intention {
    object Load : HomeIntention()
    object AddWorklog : HomeIntention()
    data class ClickedOnPermission(val permissionType: PermissionType) : HomeIntention()
}
