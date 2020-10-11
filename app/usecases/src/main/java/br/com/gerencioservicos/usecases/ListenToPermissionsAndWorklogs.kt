package br.com.gerencioservicos.usecases

import kotlinx.coroutines.flow.Flow

interface ListenToPermissionsAndWorklogs {
    operator fun invoke(): Flow<PermissionsAndWorklogs>
}
