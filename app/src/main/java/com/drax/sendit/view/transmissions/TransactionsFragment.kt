package com.drax.sendit.view.transmissions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.drax.sendit.R
import com.drax.sendit.databinding.TransmissionsFragmentBinding
import com.drax.sendit.view.TransactionWrapper
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.collect
import com.drax.sendit.view.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionsFragment: BaseFragment<TransmissionsFragmentBinding, TransactionsVM>(TransmissionsFragmentBinding::inflate) {
    override val viewModel: TransactionsVM by viewModel()

    private val transitionAdapter: TransactionAdapter by lazy {
        TransactionAdapter(
            copy = {
                copyToClipboard(it)
            },
            remove = {
                viewModel.removeTransaction(it)
            },
            share = {
                shareTransaction(it)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transitionAdapter
        }

        collect(viewModel.uiState) { uiState ->
            when(uiState){
                TransactionsUiState.Neutral -> Unit
                TransactionsUiState.NoTransaction -> {
                    binding.emptyDialog.visibility = View.VISIBLE
                    transitionAdapter.submitList(listOf())
                }
                is TransactionsUiState.TransactionsLoaded -> {
                    binding.emptyDialog.visibility = View.GONE
                    transitionAdapter.submitList(uiState.transmissions)
                }
            }
        }
    }

    private fun copyToClipboard(transactionWrapper: TransactionWrapper) {
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(
                ClipData.
                newPlainText(getString(R.string.copied_text_label), transactionWrapper.transaction.content)
            )
        toast(getString(R.string.copied_text_label))
    }

    private fun shareTransaction(transactionWrapper: TransactionWrapper) {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, transactionWrapper.transaction.content)

        }.also {
            startActivity(Intent.createChooser(it, null))
        }
    }
}