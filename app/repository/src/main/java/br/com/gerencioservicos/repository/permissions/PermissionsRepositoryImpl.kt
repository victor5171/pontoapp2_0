package br.com.gerencioservicos.repository.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

private const val PERMISSIONS_REFRESH_RATE_IN_MILLISECONDS = 1000L

internal class PermissionsRepositoryImpl(
    private val context: Context
) : PermissionsRepository {

    override fun getPermissionsState(): Flow<List<Permission>> {
        return flow {
            while (currentCoroutineContext().isActive) {
                val permissions = PermissionType.values().map {
                    Permission(it, isPermissionAllowed(it))
                }

                emit(permissions)

                delay(PERMISSIONS_REFRESH_RATE_IN_MILLISECONDS)
            }
        }
    }

    override fun isPermissionAllowed(permissionType: PermissionType): Boolean {
        val permissionName = when (permissionType) {
            PermissionType.CAMERA -> Manifest.permission.CAMERA
            PermissionType.GPS -> Manifest.permission.ACCESS_FINE_LOCATION
        }

        return ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED
    }
}
