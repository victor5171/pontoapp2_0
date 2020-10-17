package br.com.gerencioservicos.home.ui

import android.app.Activity
import android.os.Build
import br.com.gerencioservicos.home.HomeAdapterBuilder
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import io.mockk.mockkClass
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.xtras.polyadapter.testhelpers.PolyAdapterTester
import org.xtras.polyadapter.testhelpers.SealedClassItemGenerator
import org.xtras.polyadapter.testhelpers.create

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
internal class HomeAdapterBuilderTest {

    @Test
    fun `Test all the adapter methods`() {
        val activity = Robolectric.buildActivity(Activity::class.java).get()

        val allItemsGenerator = SealedClassItemGenerator.create<HomeListItem> { mockkClass(it) }

        val polyAdapterTester = PolyAdapterTester(HomeAdapterBuilder.buildAdapterBuilder(), allItemsGenerator)

        polyAdapterTester.testAll(activity) { viewTypeFromViewTypeRetriever, viewTypeFromAdapter ->
            Assert.assertEquals(viewTypeFromViewTypeRetriever, viewTypeFromAdapter)
        }
    }
}
