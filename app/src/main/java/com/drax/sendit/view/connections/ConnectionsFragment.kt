package com.drax.sendit.view.connections

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import app.siamak.sendit.R
import app.siamak.sendit.databinding.ConnectionsFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.connections.adapter.ConnectionsAdapter
import com.drax.sendit.view.connections.unpair.UnpairFragment
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import org.koin.androidx.viewmodel.ext.android.viewModel


class ConnectionsFragment :
    BaseFragment<ConnectionsFragmentBinding, ConnectionsVM>(ConnectionsFragmentBinding::inflate) {

    override val viewModel: ConnectionsVM by viewModel()

    private val adapter: ConnectionsAdapter by lazy {
        ConnectionsAdapter { connectionId ->
            analytics.set(Event.Connections.UnpairRequested)
            showUnpairBottomSheet(connectionId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.refresh.isRefreshing = false
            when (it) {
                ConnectionUiState.Neutral -> Unit
                ConnectionUiState.RefreshingConnectionList -> analytics.set(Event.Connections.RefreshedList)
                ConnectionUiState.NoConnection -> Unit
                is ConnectionUiState.ConnectionsLoaded -> adapter.newList(it.connectionList)
                is ConnectionUiState.RefreshConnectionListFailed -> {
                    analytics.set(Event.Connections.RefreshFailed)
                    modal(ModalMessage.FromNetError(it.error.errorCode))
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

        binding.menuIcon.setOnClickListener {
            showPopup(it)
        }

        viewModel.getConnectionsFromServer()
    }

    private fun showUnpairBottomSheet(connectionId: Long) {
        childFragmentManager.setFragmentResultListener(UnpairFragment.TAG, this) { _, _ ->
            viewModel.getConnectionsFromServer()
        }
        UnpairFragment(UnpairRequest(connectionId)).show(childFragmentManager, UnpairFragment.TAG)
    }

    private fun showPopup(view: View) {
        analytics.set(Event.Connections.PopupShown)
        val popupMenu = PopupMenu(requireContext(), view)

        // Inflating popup menu from popup_menu.xml file
        popupMenu.menuInflater.inflate(R.menu.connections_top_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.itemId == R.id.sign_out) {
                analytics.set(Event.Connections.Logout)
                viewModel.signOut()
            }
            true
        }
        // Showing the popup menu
        popupMenu.show()
    }
}
