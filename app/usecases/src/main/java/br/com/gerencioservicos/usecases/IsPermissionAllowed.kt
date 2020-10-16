package br.com.gerencioservicos.usecases

import br.com.gerencioservicos.repository.permissions.PermissionType

fun interface IsPermissionAllowed {
    operator fun invoke(permissionType: PermissionType): Boolean
}
