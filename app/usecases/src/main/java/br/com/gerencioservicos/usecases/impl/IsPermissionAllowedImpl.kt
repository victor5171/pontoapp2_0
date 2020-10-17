package br.com.gerencioservicos.usecases.impl

import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.repository.permissions.PermissionsRepository
import br.com.gerencioservicos.usecases.IsPermissionAllowed

internal class IsPermissionAllowedImpl(
    private val permissionsRepository: PermissionsRepository
) : IsPermissionAllowed {
    override fun invoke(permissionType: PermissionType) = permissionsRepository.isPermissionAllowed(permissionType)
}
