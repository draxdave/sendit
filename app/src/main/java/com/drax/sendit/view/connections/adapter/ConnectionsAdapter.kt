package com.drax.sendit.view.connections.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.data.db.model.Connection
import app.siamak.sendit.databinding.ItemDeviceBinding
import com.drax.sendit.domain.network.model.type.ConnectionStatus
import com.drax.sendit.domain.network.model.type.PairResponseType
import com.drax.sendit.view.DeviceWrapper

class ConnectionsAdapter(
    private val unpair : (Long) -> Unit
) : ListAdapter<DeviceWrapper, RecyclerView.ViewHolder>(
    diffCallback
)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = DeviceViewHolder(ItemDeviceBinding.inflate(LayoutInflater.from(parent.context)), unpair)

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

            override fun areContentsTheSame(oldItem: DeviceWrapper, newItem: DeviceWrapper): Boolean =
                oldItem == newItem
        }
    }

    fun newList(connectionList: List<Connection>) {
        submitList(connectionList.map {connection-> DeviceWrapper(connection) })
    }

}
class DeviceViewHolder(private val binding: ItemDeviceBinding, private val unpair: (Long) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem : DeviceWrapper) {
        with(binding) {
            deviceWrapper = deviceItem
            remove.setOnClickListener {
                unpair(deviceItem.connection.id)
            }
            executePendingBindings()
        }
    }
}