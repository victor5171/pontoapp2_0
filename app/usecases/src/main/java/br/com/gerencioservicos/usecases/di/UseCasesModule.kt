package br.com.gerencioservicos.usecases.di

import br.com.gerencioservicos.usecases.IsPermissionAllowed
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.impl.IsPermissionAllowedImpl
import br.com.gerencioservicos.usecases.impl.ListenToPermissionsAndWorklogsImpl
import org.koin.dsl.module

object UseCasesModule {
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
    }
}
