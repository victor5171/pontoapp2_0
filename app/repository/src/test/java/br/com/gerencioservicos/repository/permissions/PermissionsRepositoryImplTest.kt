package br.com.gerencioservicos.repository.permissions

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.delayEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
internal class PermissionsRepositoryImplTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When I try to get the permissions it should return them correctly`() = runBlockingTest {
        val application = ApplicationProvider.getApplicationContext<Application>()

        val permissionRepository = PermissionsRepositoryImpl(application)

        val sharedFlow = permissionRepository.getPermissionsState().take(2).shareIn(this, SharingStarted.Eagerly, 2)

        assertTrue {
            val permissions = sharedFlow.replayCache.first()
            permissions.toTypedArray().contentDeepEquals(
                arrayOf(
                    Permission(PermissionType.CAMERA, false),
                    Permission(PermissionType.GPS, false)
                )
            )
        }

        val shadowApplication = Shadows.shadowOf(application)

        shadowApplication.grantPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)

        advanceTimeBy(PERMISSIONS_REFRESH_RATE_IN_MILLISECONDS)

        assertTrue {
            val permissions = sharedFlow.replayCache[1]
            permissions.toTypedArray().contentDeepEquals(
                arrayOf(
                    Permission(PermissionType.CAMERA, true),
                    Permission(PermissionType.GPS, true)
                )
            )
        }
    }

    @Test
    fun `When I try to check if my its given permission to the camera, it should return accordingly`() {
        val application = ApplicationProvider.getApplicationContext<Application>()

        val permissionRepository = PermissionsRepositoryImpl(application)

        val shadowApplication = Shadows.shadowOf(application)

        assertFalse { permissionRepository.isPermissionAllowed(PermissionType.CAMERA) }

        shadowApplication.grantPermissions(Manifest.permission.CAMERA)

        assertTrue { permissionRepository.isPermissionAllowed(PermissionType.CAMERA) }
    }

    @Test
    fun `When I try to check if my its given permission to the gps, it should return accordingly`() {
        val application = ApplicationProvider.getApplicationContext<Application>()

        val permissionRepository = PermissionsRepositoryImpl(application)

        val shadowApplication = Shadows.shadowOf(application)

        assertFalse { permissionRepository.isPermissionAllowed(PermissionType.GPS) }

        shadowApplication.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        assertTrue { permissionRepository.isPermissionAllowed(PermissionType.GPS) }
    }
}
