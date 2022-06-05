package com.drax.sendit.view.transmissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.R
import com.drax.sendit.databinding.ItemTransactionBinding
import com.drax.sendit.view.TransactionWrapper

class TransactionAdapter(
    private val copy: (TransactionWrapper) -> Unit,
    private val remove: (TransactionWrapper) -> Unit,
    private val share: (TransactionWrapper) -> Unit,
) : ListAdapter<TransactionWrapper, RecyclerView.ViewHolder>(
    diffCallback
)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = TransactionViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context)),
    copy, remove, share)

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
class TransactionViewHolder(private val binding: ItemTransactionBinding,
                            private val copy: (TransactionWrapper) -> Unit,
                            private val remove: (TransactionWrapper) -> Unit,
                            private val share: (TransactionWrapper) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnLongClickListener {
            showPopup(it)
            false
        }
    }

    fun bindTo(deviceItem : TransactionWrapper) {
        with(binding) {
            transaction = deviceItem
            executePendingBindings()
        }
    }


    private fun showPopup(view: View){
        val popupMenu = PopupMenu(view.context, view)

        // Inflating popup menu from popup_menu.xml file
        popupMenu.menuInflater.inflate(R.menu.transactions_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.transaction?.let { transition ->
                when (menuItem.itemId) {
                    R.id.copy -> copy(transition)
                    R.id.share -> share(transition)
                    R.id.remove -> remove(transition)
                    else -> Unit
                }
            }
            true
        }
        // Showing the popup menu
        popupMenu.show()
    }
}