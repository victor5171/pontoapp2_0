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
        val separator = if (permissionType.size <= 2) { " ${context.getString(R.string.two_items_separator)}" } else ","
        return permissionType.joinToString(separator = "$separator ") {
            context.getString(translate(it))
        }
    }
}
