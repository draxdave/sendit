package com.drax.sendit.view.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drax.sendit.R
import com.drax.sendit.databinding.ItemTransactionBinding
import com.drax.sendit.view.MessageWrapper

class TransactionAdapter(
    private val copy: (MessageWrapper) -> Unit,
    private val remove: (MessageWrapper) -> Unit,
    private val share: (MessageWrapper) -> Unit,
) : ListAdapter<MessageWrapper, RecyclerView.ViewHolder>(
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
        private val diffCallback = object : DiffUtil.ItemCallback<MessageWrapper>() {
            override fun areItemsTheSame(oldItem: MessageWrapper, newItem: MessageWrapper): Boolean =
                oldItem.message.id == newItem.message.id

            override fun areContentsTheSame(oldItem: MessageWrapper, newItem: MessageWrapper): Boolean =
                oldItem == newItem
        }
    }

}
class TransactionViewHolder(private val binding: ItemTransactionBinding,
                            private val copy: (MessageWrapper) -> Unit,
                            private val remove: (MessageWrapper) -> Unit,
                            private val share: (MessageWrapper) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            showPopup(it)
        }
    }

    fun bindTo(deviceItem : MessageWrapper) {
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