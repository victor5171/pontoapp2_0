package br.com.gerencioservicos.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.repository.permissions.Permission
import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.styles.compose.Paddings

@Composable
internal fun PermissionItemColumn(permission: Permission, onClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Paddings.Tiny, horizontal = Paddings.Tiny)
    ) {
        val (imageRef, permissionRef, permissionStateRef) = createRefs()
        val permissionName = stringResource(PermissionNameTranslator.translate(permission.permissionType))

        Image(
            modifier = Modifier.size(Dimens.ListIconSize).constrainAs(imageRef) {
                start.linkTo(parent.start)
                end.linkTo(permissionRef.start, Paddings.Tiny)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            painter = painterResource(if (permission.isGiven) { R.drawable.ic__08_tick_mark } else { R.drawable.ic__76_warning }),
            contentDescription = null
        )
        Text(
            modifier = Modifier.constrainAs(permissionRef) {
                start.linkTo(imageRef.end)
                end.linkTo(permissionStateRef.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            },
            text = stringResource(id = R.string.permission_for, permissionName),
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.constrainAs(permissionStateRef) {
                start.linkTo(permissionRef.end, Paddings.Tiny)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
            text = stringResource(if (permission.isGiven) { R.string.given } else { R.string.not_given }),
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview
@Composable
private fun PermissionPreview() {
    PermissionItemColumn(permission = Permission(PermissionType.CAMERA, true), onClick = {})
}
