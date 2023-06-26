package com.drax.sendit.view.connections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.drax.sendit.domain.network.model.UnpairRequest
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.connections.unpair.UnpairFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectionsFragment : BaseComposeFragment() {

    val viewModel: ConnectionsVM by viewModels()

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
        val uiState by viewModel.uiState
        val deviceInfo by viewModel.deviceInfo
        val userInfo by viewModel.userInfo

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .verticalScroll(scrollState)
        ) {
            ConnectionsHeader(
                email = userInfo?.email ?: "",
                deviceInfo = deviceInfo?.name ?: "",
                lastUsed = deviceInfo?.lastTouch ?: "",
            ) {
                // remove device
                viewModel.signOut()
            }

            ConnectionList(
                uiState = uiState,
                onRefresh = {
                    viewModel.getConnectionsFromServer()
                }
            ) { connection ->
                showUnpairBottomSheet(connection.id)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ConnectionsScreenPreview() {
        ConnectionsScreen()
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

}
