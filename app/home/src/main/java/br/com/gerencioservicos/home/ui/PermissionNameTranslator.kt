package br.com.gerencioservicos.home.ui

import android.content.Context
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.repository.permissions.PermissionType

internal object PermissionNameTranslator {
    fun translate(permissionType: PermissionType) = when (permissionType) {
        PermissionType.CAMERA -> R.string.camera
        PermissionType.GPS -> R.string.gps
    }

    fun translateAllWithComma(context: Context, permissionType: Collection<PermissionType>): String {
        return permissionType.joinToString(separator = ",") {
            context.getString(translate(it))
        }
    }
}
