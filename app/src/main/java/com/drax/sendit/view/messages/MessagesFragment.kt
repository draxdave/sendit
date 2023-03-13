package com.drax.sendit.view.messages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import app.siamak.sendit.databinding.TransmissionsFragmentBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.drax.sendit.data.service.Event
import com.drax.sendit.view.MessageWrapper
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.observe
import com.drax.sendit.view.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment :
    BaseFragment<TransmissionsFragmentBinding, MessagesVM>(TransmissionsFragmentBinding::inflate) {
    override val viewModel: MessagesVM by viewModels()

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
        initObservers()
        initView()
    }

    private fun initObservers() {

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
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

        viewModel.messagesList.observe(viewLifecycleOwner) {
            transitionAdapter.submitList(it)
        }
    }

    private fun initView() {
        binding.list.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                true
            )
            adapter = transitionAdapter
        }

        Glide.with(binding.listBg)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .load(CHAT_BG_IMG_URL)
            .into(binding.listBg)
    }

    private fun copyToClipboard(messageWrapper: MessageWrapper) {
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)
            ?.setPrimaryClip(
                ClipData.newPlainText(
                    getString(R.string.copied_text_label),
                    messageWrapper.message.content
                )
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

    companion object {
        private const val CHAT_BG_IMG_URL = "${BuildConfig.BASE_URL}/img/chat_background_01.jpg"
    }
}