package br.com.gerencioservicos.pontoapp.di

import br.com.gerencioservicos.pontoapp.BuildConfig
import br.com.gerencioservicos.usecases.di.UseCasesModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal object AppModule {
    val module = module {
        single(named(UseCasesModule.VERSION_KEY)) { BuildConfig.VERSION_NAME }
    }
}
