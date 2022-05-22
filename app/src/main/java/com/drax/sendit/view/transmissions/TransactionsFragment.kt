package com.drax.sendit.view.transmissions

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.drax.sendit.databinding.TransmissionsFragmentBinding
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.util.collect
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionsFragment: BaseFragment<TransmissionsFragmentBinding, TransactionsVM>(TransmissionsFragmentBinding::inflate) {
    override val viewModel: TransactionsVM by viewModel()

    private val transitionAdapter: TransactionAdapter by lazy {
        TransactionAdapter()
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

        collect(viewModel.uiState) {uiState->
                when(uiState){
                    TransactionsUiState.Neutral -> Unit
                    TransactionsUiState.NoTransaction -> binding.emptyDialog.visibility = View.VISIBLE
                    is TransactionsUiState.TransactionsLoaded -> {
                        binding.emptyDialog.visibility = View.GONE
                        transitionAdapter.submitList(uiState.transmissions)
                    }
                }
        }
    }
}