package com.drax.sendit.view.connections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.siamak.sendit.R
import app.siamak.sendit.databinding.ItemDeviceBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drax.sendit.view.DeviceWrapper

class ConnectionsAdapter(
    private val unpair: (Long) -> Unit
) : ListAdapter<DeviceWrapper, RecyclerView.ViewHolder>(
    diffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context)), unpair)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as DeviceViewHolder).bindTo(it)
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

    fun newList(connectionList: List<DeviceWrapper>) {
        submitList(connectionList)
    }
}

class DeviceViewHolder(private val binding: ItemDeviceBinding, private val unpair: (Long) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem: DeviceWrapper) {
        with(binding) {
            deviceWrapper = deviceItem
            remove.setOnClickListener {
                unpair(deviceItem.connection.id)
            }
            Glide.with(icon)
                .load(deviceItem.connection.iconUrl)
                .placeholder(R.drawable.default_device_placeholder)
                .error(R.drawable.default_device_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(icon)
            executePendingBindings()
        }
    }
}
