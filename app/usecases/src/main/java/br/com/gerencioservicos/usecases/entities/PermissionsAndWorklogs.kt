package br.com.gerencioservicos.usecases.entities

import br.com.gerencioservicos.repository.permissions.Permission

data class PermissionsAndWorklogs(
    val permissions: List<Permission>,
    val numberOfPendingWorklogs: Int
)
