package com.drax.sendit.view.connections

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.databinding.ConnectionsFragmentBinding
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.connections.adapter.ConnectionsAdapter
import com.drax.sendit.view.util.modal
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConnectionsFragment : BaseFragment<ConnectionsFragmentBinding,ConnectionsVM>(ConnectionsFragmentBinding::inflate) {

    override val viewModel: ConnectionsVM by viewModel()

    private val adapter : ConnectionsAdapter by lazy { ConnectionsAdapter { connection_id ->
        viewModel.removeDevice(UnpairRequest(connection_id))
    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                binding.refresh.isRefreshing = false
                when(it){
                    ConnectionUiState.Neutral -> Unit
                    ConnectionUiState.RefreshConnectionListSucceedButEmpty -> Unit
                    ConnectionUiState.RefreshingConnectionList -> Unit
                    ConnectionUiState.NoConnection -> Unit
                    is ConnectionUiState.ConnectionsLoaded -> adapter.newList(it.connectionList)
                    is ConnectionUiState.RefreshConnectionListFailed -> modal(ModalMessage.FromNetError(it.error.errorCode))

                    is ConnectionUiState.RefreshConnectionListSucceed -> adapter.newList(it.connectionList)
                }
            }
        }
        binding.list.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ConnectionsFragment.adapter
        }

        binding.refresh.setOnRefreshListener {
            viewModel.getConnectionsFromServer()
        }
    }
}