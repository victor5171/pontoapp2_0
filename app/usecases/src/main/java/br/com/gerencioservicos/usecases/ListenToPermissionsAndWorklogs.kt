package br.com.gerencioservicos.usecases

import br.com.gerencioservicos.usecases.entities.PermissionsAndWorklogs
import kotlinx.coroutines.flow.Flow

fun interface ListenToPermissionsAndWorklogs {
    operator fun invoke(): Flow<PermissionsAndWorklogs>
}
