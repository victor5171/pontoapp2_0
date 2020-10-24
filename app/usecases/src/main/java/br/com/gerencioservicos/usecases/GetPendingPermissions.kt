package br.com.gerencioservicos.usecases

import br.com.gerencioservicos.repository.permissions.PermissionType

interface GetPendingPermissions {
    operator fun invoke(): Collection<PermissionType>
}
