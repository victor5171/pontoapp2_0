package br.com.gerencioservicos.home.viewmodel

import androidx.annotation.Keep
import br.com.gerencioservicos.repository.permissions.Permission

@Keep
internal sealed class HomeListItem {
    abstract fun compareIdentity(other: HomeListItem): Boolean

    data class PermissionListItem(val permission: Permission) : HomeListItem() {

        override fun compareIdentity(other: HomeListItem): Boolean {
            if (other is PermissionListItem) {
                return permission.permissionType == other.permission.permissionType
            }

            return false
        }
    }

    data class PendingSynchronizationListItem(val numberOfPendingItems: Int) : HomeListItem() {
        override fun compareIdentity(other: HomeListItem): Boolean {
            return other is PendingSynchronizationListItem
        }
    }
}
