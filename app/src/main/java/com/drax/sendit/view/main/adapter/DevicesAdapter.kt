package com.drax.sendit.view.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.data.db.model.Device
import com.drax.sendit.databinding.ItemDeviceBinding

class DevicesAdapter(private val onRemove : (String) -> Unit) : ListAdapter<Device, RecyclerView.ViewHolder>(
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
        private val diffCallback = object : DiffUtil.ItemCallback<Device>() {
            override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean =
                oldItem.instanceId == newItem.instanceId

            override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean =
                oldItem == newItem
        }
    }

}
class DeviceViewHolder( val binding : ItemDeviceBinding ,val onRemove : (String) -> Unit ) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem : Device) {
        with(binding) {
            device = deviceItem
            remove.setOnClickListener { onRemove(deviceItem.instanceId) }
            executePendingBindings()
        }
    }
}