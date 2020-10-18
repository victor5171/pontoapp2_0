package br.com.gerencioservicos.usecases.di

import br.com.gerencioservicos.usecases.IsPermissionAllowed
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.RetrieveVersion
import br.com.gerencioservicos.usecases.impl.IsPermissionAllowedImpl
import br.com.gerencioservicos.usecases.impl.ListenToPermissionsAndWorklogsImpl
import br.com.gerencioservicos.usecases.impl.RetrieveVersionImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

object UseCasesModule {
    const val VERSION_KEY = "version"

    val module = module {
        single<IsPermissionAllowed> {
            IsPermissionAllowedImpl(
                permissionsRepository = get()
            )
        }
        single<ListenToPermissionsAndWorklogs> {
            ListenToPermissionsAndWorklogsImpl(
                permissionsRepository = get()
            )
        }
        single<RetrieveVersion> {
            RetrieveVersionImpl(get(named(VERSION_KEY)))
        }
    }
}
