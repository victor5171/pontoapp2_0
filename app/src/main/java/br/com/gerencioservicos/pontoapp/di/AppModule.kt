package br.com.gerencioservicos.pontoapp.di

import br.com.gerencioservicos.pontoapp.BuildConfig
import br.com.gerencioservicos.pontoapp.logger.StubLogger
import br.com.gerencioservicos.usecases.di.UseCasesModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.xtras.mvi.Logger

internal object AppModule {
    val module = module {
        single(named(UseCasesModule.VERSION_KEY)) { BuildConfig.VERSION_NAME }
        single<Logger> { StubLogger }
    }
}
