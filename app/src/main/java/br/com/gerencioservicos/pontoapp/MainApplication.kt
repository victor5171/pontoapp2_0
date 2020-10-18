package br.com.gerencioservicos.pontoapp

import android.app.Application
import br.com.gerencioservicos.pontoapp.di.KoinSetupManager
import org.koin.core.context.startKoin

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startDependencyInjection()
    }

    private fun startDependencyInjection() = startKoin {
        KoinSetupManager.setup(this@MainApplication, this)
    }
}
