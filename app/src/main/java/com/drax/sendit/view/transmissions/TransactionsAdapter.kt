package com.drax.sendit.view.transmissions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.Transaction
import com.drax.sendit.databinding.ItemDeviceBinding
import com.drax.sendit.databinding.ItemTransactionBinding
import com.drax.sendit.view.TransactionWrapper

class TransactionAdapter() : ListAdapter<TransactionWrapper, RecyclerView.ViewHolder>(
    diffCallback
)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = TransactionViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as TransactionViewHolder).bindTo(it)
        }
    }

    companion object {
        //This diff callback informs the PagedListAdapter how to compute list differences when new
        private val diffCallback = object : DiffUtil.ItemCallback<TransactionWrapper>() {
            override fun areItemsTheSame(oldItem: TransactionWrapper, newItem: TransactionWrapper): Boolean =
                oldItem.transaction.id == newItem.transaction.id

            override fun areContentsTheSame(oldItem: TransactionWrapper, newItem: TransactionWrapper): Boolean =
                oldItem == newItem
        }
    }

}
class TransactionViewHolder(private val binding : ItemTransactionBinding ) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(deviceItem : TransactionWrapper) {
        with(binding) {
            transaction = deviceItem
            executePendingBindings()
        }
    }
}