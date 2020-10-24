package br.com.gerencioservicos.usecases.impl

import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.repository.permissions.PermissionsRepository
import br.com.gerencioservicos.usecases.GetPendingPermissions

internal class GetPendingPermissionsImpl(
    private val permissionsRepository: PermissionsRepository
) : GetPendingPermissions {
    override fun invoke(): Collection<PermissionType> {
        return PermissionType.values().filterNot(permissionsRepository::isPermissionAllowed)
    }
}
