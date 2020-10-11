package br.com.gerencioservicos.repository.worklog

import kotlinx.coroutines.flow.Flow

interface WorklogRepository {
    fun getNumberOfWorklogsToSynchronize(): Flow<Int>
}
