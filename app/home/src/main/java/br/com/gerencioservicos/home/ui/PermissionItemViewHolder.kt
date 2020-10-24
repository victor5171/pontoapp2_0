package br.com.gerencioservicos.home.ui

import android.view.ViewGroup
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.home.viewmodel.HomeListItem
import br.com.gerencioservicos.repository.permissions.PermissionType
import kotlinx.android.synthetic.main.list_item_permission.view.*
import org.xtras.polyadapter.BindableViewHolder

internal class PermissionItemViewHolder(
    parent: ViewGroup,
    private val onPermissionClick: (PermissionType) -> Unit
) : BindableViewHolder<HomeListItem.PermissionListItem>(
    parent,
    R.layout.list_item_permission
) {

    private lateinit var item: HomeListItem.PermissionListItem

    init {
        itemView.setOnClickListener {
            onPermissionClick(item.permission.permissionType)
        }
    }

    override fun bind(item: HomeListItem.PermissionListItem) {
        this.item = item

        with(itemView) {
            val permissionName = resources.getString(PermissionNameTranslator.translate(item.permission.permissionType))

            textViewPermissionText.text = resources.getString(R.string.permission_for, permissionName)

            val iconResourceId = if (item.permission.isGiven) {
                R.drawable.ic__08_tick_mark
            } else {
                R.drawable.ic__76_warning
            }

            imageViewIcon.setImageResource(iconResourceId)

            textViewPermissionStatus.setText(if(item.permission.isGiven) {
                R.string.given
            } else {
                R.string.not_given
            })
        }
    }
}
