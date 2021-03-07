package br.com.gerencioservicos.repository.di

import br.com.gerencioservicos.repository.permissions.PermissionsRepository
import br.com.gerencioservicos.repository.permissions.PermissionsRepositoryImpl
import br.com.gerencioservicos.repository.qrcode.QrcodeRepository
import br.com.gerencioservicos.repository.qrcode.QrcodeRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object RepositoryModule {
    val module = module {
        single<PermissionsRepository> { PermissionsRepositoryImpl(androidApplication()) }
        single<QrcodeRepository> { QrcodeRepositoryImpl() }
    }
}
