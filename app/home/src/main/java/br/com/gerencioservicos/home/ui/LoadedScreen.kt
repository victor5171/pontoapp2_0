package br.com.gerencioservicos.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import br.com.gerencioservicos.home.viewmodel.HomeState
import br.com.gerencioservicos.repository.permissions.Permission
import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.styles.compose.AppDivider
import br.com.gerencioservicos.styles.compose.Paddings
import org.xtras.mvi.Actions

@Composable
internal fun LoadedScreen(loadedState: HomeState.Loaded, onItemSelected: (PermissionType) -> Unit) {
    Column {
        List(loadedState.homeListItems, onItemSelected = onItemSelected)
        Version(loadedState.version)
    }
}

@Composable
private fun ColumnScope.List(listItems: List<HomeListItem>, onItemSelected: (PermissionType) -> Unit) {
    LazyColumn(
        modifier = Modifier.weight(1.0f),
    ) {
        itemsIndexed(listItems) { index, item ->
            when(item) {
                is HomeListItem.PendingSynchronizationListItem -> TODO()
                is HomeListItem.PermissionListItem -> PermissionItemColumn(item.permission, onClick = {
                    onItemSelected(item.permission.permissionType)
                })
            }

            if (index < listItems.size - 1) {
                AppDivider()
            }
        }
    }
}

@Preview
@Composable
private fun Version(version: String = "1.0.0") {
    Text(
        modifier = Modifier.padding(start = Paddings.Tiny, top = Paddings.Tiny, bottom = Paddings.Tiny),
        text = version,
        style = MaterialTheme.typography.body1
    )
}

@Preview
@Composable
private fun LoadedScreenPreview() {
    LoadedScreen(
        loadedState = HomeState.Loaded(
            version = "1.0.0",
            homeListItems = listOf(HomeListItem.PermissionListItem(Permission(PermissionType.CAMERA, true))),
            actions = Actions(),
            actionError = null
        ),
        onItemSelected = {}
    )
}
