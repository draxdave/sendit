package com.drax.sendit.view.connections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import app.siamak.sendit.R
import app.siamak.sendit.databinding.ConnectionsFragmentBinding
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseFragment
import com.drax.sendit.view.connections.adapter.ConnectionsAdapter
import com.drax.sendit.view.connections.unpair.UnpairFragment
import com.drax.sendit.view.util.loadImageFromUri
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class ConnectionsFragment :
    BaseFragment<ConnectionsFragmentBinding, ConnectionsVM>(ConnectionsFragmentBinding::inflate) {

    override val viewModel: ConnectionsVM by viewModels()

    private val adapter: ConnectionsAdapter by lazy {
        ConnectionsAdapter { connectionId ->
            analytics.set(Event.Connections.UnpairRequested)
            showUnpairBottomSheet(connectionId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext(), null, 0).apply {
            setContent {
                ConnectionsScreen()
            }
        }
    }

    @Composable
    fun ConnectionsScreen() {
        Column {
            Text(text = "Connections")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initObservers() {
        val context = context ?: return
        viewModel.device.filterNotNull()
            .map {
                DeviceTransformer.toUiModel(context, it)
            }
            .observe(viewLifecycleOwner) {
                updateDeviceUi(it)
            }
    }

    private fun updateDeviceUi(deviceDomain: DeviceUiModel) = with(binding) {
        deviceIcon.loadImageFromUri(deviceDomain.iconUrl)
        lastUsed.text = deviceDomain.lastTouch
        deviceName.text = deviceDomain.name
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
        UnpairFragment().apply {
            arguments = Bundle().apply {
                putSerializable(UnpairFragment.FRAGMENT_KEY, UnpairRequest(connectionId))
            }
        }.show(childFragmentManager, UnpairFragment.TAG)
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
