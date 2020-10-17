package br.com.gerencioservicos.pontoapp

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import br.com.gerencioservicos.pontoapp.di.KoinSetupManager
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.check.checkModules
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
internal class AllModulesTest {
    @Test
    fun checkAllModules() = checkModules {
        KoinSetupManager.setup(ApplicationProvider.getApplicationContext(), this)
    }
}
