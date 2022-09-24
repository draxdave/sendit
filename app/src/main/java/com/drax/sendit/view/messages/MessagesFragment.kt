package com.drax.sendit.view.messages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.siamak.sendit.R
import com.drax.sendit.data.service.Event
import app.siamak.sendit.databinding.TransmissionsFragmentBinding
import com.drax.sendit.view.MessageWrapper
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.collect
import com.drax.sendit.view.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MessagesFragment: BaseFragment<TransmissionsFragmentBinding, MessagesVM>(TransmissionsFragmentBinding::inflate) {
    override val viewModel: MessagesVM by viewModel()

    private val transitionAdapter: TransactionAdapter by lazy {
        TransactionAdapter(
            copy = {
                analytics.set(Event.Messages.Copy)
                copyToClipboard(it)
            },
            remove = {
                analytics.set(Event.Messages.Remove)
                viewModel.removeTransaction(it)
            },
            share = {
                analytics.set(Event.Messages.Share)
                shareTransaction(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
//        (newIntent.getSerializableExtra(NotificationUtil.NOTIFICATION_DATA) as? NotificationModel)?.let { notificationModel ->
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transitionAdapter
        }

        collect(viewModel.uiState) { uiState ->
            when(uiState){
                MessagesUiState.Neutral -> Unit
                MessagesUiState.NoTransaction -> {
                    binding.emptyDialog.visibility = View.VISIBLE
                    transitionAdapter.submitList(listOf())
                }
                is MessagesUiState.MessagesLoaded -> {
                    binding.emptyDialog.visibility = View.GONE
                    transitionAdapter.submitList(uiState.transmissions)
                }
            }
        }

        collect(viewModel.messagesList) {
            transitionAdapter.submitList(it)
        }
    }

    private fun copyToClipboard(messageWrapper: MessageWrapper) {
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(
                ClipData.
                newPlainText(getString(R.string.copied_text_label), messageWrapper.message.content)
            )
        toast(getString(R.string.copied_text_label))
    }

    private fun shareTransaction(messageWrapper: MessageWrapper) {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, messageWrapper.message.content)

        }.also {
            startActivity(Intent.createChooser(it, null))
        }
    }
}