package br.com.gerencioservicos.repository.permissions

data class Permission(
    val permissionType: PermissionType,
    val isGiven: Boolean
)
