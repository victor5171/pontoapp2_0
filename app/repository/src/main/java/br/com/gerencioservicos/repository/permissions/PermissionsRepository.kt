package br.com.gerencioservicos.repository.permissions

import kotlinx.coroutines.flow.Flow

interface PermissionsRepository {
    fun getPermissionsState(): Flow<List<Permission>>
    fun isPermissionAllowed(permissionType: PermissionType): Boolean
}
