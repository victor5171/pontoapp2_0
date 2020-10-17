package br.com.gerencioservicos.usecases.impl

import br.com.gerencioservicos.repository.permissions.PermissionsRepository
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ListenToPermissionsAndWorklogsImpl(
    private val permissionsRepository: PermissionsRepository
) : ListenToPermissionsAndWorklogs {
    override fun invoke(): Flow<PermissionsAndWorklogs> {
        return permissionsRepository.getPermissionsState()
            .map {
                PermissionsAndWorklogs(
                    permissions = it,
                    numberOfPendingWorklogs = 0
                )
            }
    }
}
