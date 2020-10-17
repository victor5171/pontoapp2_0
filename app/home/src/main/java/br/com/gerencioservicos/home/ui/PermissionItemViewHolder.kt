package br.com.gerencioservicos.home.ui

import android.view.ViewGroup
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import org.xtras.polyadapter.BindableViewHolder

internal class PermissionItemViewHolder(parent: ViewGroup) : BindableViewHolder<HomeListItem.PermissionListItem>(
    parent,
    R.layout.list_item_permission
) {

    override fun bind(item: HomeListItem.PermissionListItem) {
    }
}
