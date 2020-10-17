package br.com.gerencioservicos.repository.di

import br.com.gerencioservicos.repository.permissions.PermissionsRepository
import br.com.gerencioservicos.repository.permissions.PermissionsRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object RepositoryModule {
    val module = module {
        single<PermissionsRepository> { PermissionsRepositoryImpl(androidApplication()) }
    }
}
