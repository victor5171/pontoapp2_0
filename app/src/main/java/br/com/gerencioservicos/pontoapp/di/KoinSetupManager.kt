package br.com.gerencioservicos.pontoapp.di

import android.app.Application
import br.com.gerencioservicos.home.di.HomeModule
import br.com.gerencioservicos.navigation.di.NavigationModule
import br.com.gerencioservicos.qrcodescanner.di.QrcodeModule
import br.com.gerencioservicos.repository.di.RepositoryModule
import br.com.gerencioservicos.usecases.di.UseCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level

internal object KoinSetupManager {
    fun setup(application: Application, koinApplication: KoinApplication) = with(koinApplication) {
        androidContext(application)
        androidLogger(Level.ERROR)
        modules(
            AppModule.module,
            NavigationModule.module,
            UseCasesModule.module,
            RepositoryModule.module,
            HomeModule.module,
            QrcodeModule.module
        )
    }
}
