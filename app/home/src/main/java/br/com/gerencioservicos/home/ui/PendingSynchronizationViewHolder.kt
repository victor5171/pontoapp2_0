package br.com.gerencioservicos.home.ui

import android.view.ViewGroup
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import org.xtras.polyadapter.BindableViewHolder

internal class PendingSynchronizationViewHolder(parent: ViewGroup) : BindableViewHolder<HomeListItem.PendingSynchronizationListItem>(
    parent,
    R.layout.list_item_pending_synchronization
) {

    override fun bind(item: HomeListItem.PendingSynchronizationListItem) {
    }
}
