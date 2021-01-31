package br.com.gerencioservicos.home.ui

import android.app.Activity
import br.com.gerencioservicos.home.HomeAdapterBuilder
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import br.com.gerencioservicos.repository.permissions.PermissionType
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.robolectric.RobolectricTest
import io.kotest.matchers.shouldBe
import io.mockk.mockkClass
import org.robolectric.Robolectric
import org.xtras.polyadapter.testhelpers.PolyAdapterTester
import org.xtras.polyadapter.testhelpers.SealedClassItemGenerator
import org.xtras.polyadapter.testhelpers.create

@RobolectricTest
internal class HomeAdapterBuilderTest : FunSpec({

    test("Test") {
        val activity = Robolectric.buildActivity(Activity::class.java).get()
        activity.setTheme(R.style.AppTheme)

        val allItemsGenerator = SealedClassItemGenerator.create<HomeListItem> { mockkClass(it, relaxed = true) }

        val onPermissionClick: (PermissionType) -> Unit = {}

        val polyAdapterTester = PolyAdapterTester(HomeAdapterBuilder.buildAdapterBuilder(onPermissionClick), allItemsGenerator)

        polyAdapterTester.testAll(activity) { viewTypeFromViewTypeRetriever, viewTypeFromAdapter ->
            viewTypeFromViewTypeRetriever.shouldBe(viewTypeFromAdapter)
        }
    }
})
