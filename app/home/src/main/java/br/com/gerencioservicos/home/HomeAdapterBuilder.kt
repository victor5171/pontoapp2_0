package br.com.gerencioservicos.home

import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.DiffUtil
import br.com.gerencioservicos.home.ui.PendingSynchronizationViewHolder
import br.com.gerencioservicos.home.ui.PermissionItemViewHolder
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import br.com.gerencioservicos.repository.permissions.PermissionType
import org.xtras.polyadapter.BindableViewHolderDelegate
import org.xtras.polyadapter.PolyAdapterBuilder
import org.xtras.polyadapter.viewtyperetrievers.ClassViewTypeRetriever
import org.xtras.polyadapter.viewtyperetrievers.registerDelegate

internal object HomeAdapterBuilder {
    @VisibleForTesting
    fun buildAdapterBuilder(onPermissionClick: (PermissionType) -> Unit): PolyAdapterBuilder<HomeListItem, ClassViewTypeRetriever<HomeListItem>> {
        val classViewTypeRetriever = ClassViewTypeRetriever<HomeListItem>()

        return PolyAdapterBuilder(classViewTypeRetriever)
            .registerDelegate(BindableViewHolderDelegate { PermissionItemViewHolder(it, onPermissionClick) })
            .registerDelegate(BindableViewHolderDelegate { PendingSynchronizationViewHolder(it) })
    }

    fun buildForListAdapter(onPermissionClick: (PermissionType) -> Unit) = buildAdapterBuilder(onPermissionClick).buildForListAdapter(itemCallback)
}

private val itemCallback = object : DiffUtil.ItemCallback<HomeListItem>() {
    override fun areItemsTheSame(oldItem: HomeListItem, newItem: HomeListItem): Boolean {
        return oldItem.compareIdentity(newItem)
    }

    override fun areContentsTheSame(oldItem: HomeListItem, newItem: HomeListItem): Boolean {
        return oldItem == newItem
    }
}
