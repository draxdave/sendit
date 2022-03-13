package com.drax.sendit.view.devices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.databinding.ItemDeviceBinding
import com.drax.sendit.view.DeviceWrapper

class DevicesAdapter(private val onRemove : (Long) -> Unit) : ListAdapter<DeviceWrapper, RecyclerView.ViewHolder>(
    diffCallback
)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context)), onRemove)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as DeviceViewHolder).bindTo(it)
        }
    }

    companion object {
        //This diff callback informs the PagedListAdapter how to compute list differences when new
        private val diffCallback = object : DiffUtil.ItemCallback<DeviceWrapper>() {
            override fun areItemsTheSame(oldItem: DeviceWrapper, newItem: DeviceWrapper): Boolean =
                oldItem.device.id == newItem.device.id

            override fun areContentsTheSame(oldItem: DeviceWrapper, newItem: DeviceWrapper): Boolean =
                oldItem == newItem
        }
    }

}
class DeviceViewHolder(private val binding : ItemDeviceBinding, val onRemove : (Long) -> Unit ) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem : DeviceWrapper) {
        with(binding) {
            deviceWrapper = deviceItem
            remove.setOnClickListener { onRemove(deviceItem.device.id) }
            executePendingBindings()
        }
    }
}