package com.drax.sendit.view.shareContent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.siamak.sendit.databinding.ItemShareConnectionBinding
import com.drax.sendit.view.DeviceWrapper

class ShareConnectionsAdapter(
    private val onClick: (DeviceWrapper) -> Unit,
) : ListAdapter<DeviceWrapper, RecyclerView.ViewHolder>(
    diffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShareConnectionsVH(
        ItemShareConnectionBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as ShareConnectionsVH).bindTo(it)
        }
    }

    companion object {
        //This diff callback informs the PagedListAdapter how to compute list differences when new
        private val diffCallback = object : DiffUtil.ItemCallback<DeviceWrapper>() {
            override fun areItemsTheSame(oldItem: DeviceWrapper, newItem: DeviceWrapper): Boolean =
                oldItem.connection.id == newItem.connection.id

            override fun areContentsTheSame(
                oldItem: DeviceWrapper,
                newItem: DeviceWrapper
            ): Boolean =
                oldItem == newItem
        }
    }

}

class ShareConnectionsVH(
    private val binding: ItemShareConnectionBinding,
    private val onClick: (DeviceWrapper) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem: DeviceWrapper) {
        with(binding) {
            deviceWrapper = deviceItem
            root.setOnClickListener { onClick.invoke(deviceItem) }
            executePendingBindings()
        }
    }
}