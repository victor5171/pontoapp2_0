package br.com.gerencioservicos.usecases

import kotlinx.coroutines.flow.Flow

fun interface ListenToPermissionsAndWorklogs {
    operator fun invoke(): Flow<PermissionsAndWorklogs>
}
