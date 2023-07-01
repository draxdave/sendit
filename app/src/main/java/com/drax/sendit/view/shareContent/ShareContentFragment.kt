package com.drax.sendit.view.shareContent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import app.siamak.sendit.R
import com.drax.sendit.data.model.ModalMessage
import com.drax.sendit.data.service.Event
import com.drax.sendit.domain.network.model.type.DevicePlatform
import com.drax.sendit.view.base.BaseComposeBottomSheet
import com.drax.sendit.view.shareContent.components.ShareContentList
import com.drax.sendit.view.util.modal
import com.drax.sendit.view.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareContentFragment : BaseComposeBottomSheet() {
    val viewModel: ShareContentVM by viewModels()

    private val contentToShare: String? by lazy {
        arguments?.getString(SHARE_CONTENT_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            ShareContentScreen()
        }
    }

    @Composable
    fun ShareContentScreen() {
        val viewState = viewModel.uiState.collectAsState()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                ShareContentUiState.Loading -> Unit
                is ShareContentUiState.ConnectionsLoaded -> Unit
                ShareContentUiState.NoConnectionsAvailable -> Unit
                ShareContentUiState.SharingDone -> {
                    analytics.set(Event.Share.Sent)
                    modal(ModalMessage.Success(R.string.share_success)) {
                        setResultAndDismiss(TAG, bundleOf())
                    }
                }

                is ShareContentUiState.SharingFailed -> {
                    analytics.set(Event.Share.Failed(uiState.reason.errorCode.toString()))
                    modal(ModalMessage.FromNetError(uiState.reason.errorCode))
                }
            }
        }
        ShareContentScreen(viewState) { connectionId ->
            contentToShare?.let {
                viewModel.share(
                    content = it,
                    connectionId = connectionId
                )
            }
        }
    }

    @Composable
    fun ShareContentScreen(uiStateProvider: State<ShareContentUiState>, share: (Long) -> Unit) {
        val uiState by uiStateProvider

        Log.e("ShareContentScreen", "uiState: $uiState")

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.sendit),
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp),
                    text = stringResource(id = R.string.share_modal_title),
                    style = MaterialTheme.typography.body1,
                )
                Divider(color = Color.Gray.copy(alpha = .3f), thickness = 1.dp)

                ShareContentList(
                    modifier = Modifier.padding(top = 16.dp),
                    uiState = uiState, itemSelected = share
                )
            }
            Image(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_share_24),
                contentDescription = null
            )

            if (uiState is ShareContentUiState.Loading)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background.copy(alpha = 0.3f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(MaterialTheme.colors.background.copy(alpha = 0.3f)),
                        color = MaterialTheme.colors.primary,
                    )
                }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun ShareContentScreenPreview() {
        val state = remember {
            mutableStateOf(ShareContentUiState.NoConnectionsAvailable)
        }
        ShareContentScreen(
            uiStateProvider = state
        ) {
            // do nothing
        }
    }

    @Suppress("unused")
    private fun platformToIcon(@DevicePlatform platform: Int) = when (platform) {
        DevicePlatform.DevicePlatform_ANDROID -> R.drawable.ic_round_android_24
        DevicePlatform.DevicePlatform_CHROME -> R.drawable.ic_google_icon
        else -> R.drawable.ic_fragment_devices

    }

    companion object {
        const val SHARE_CONTENT_KEY = "SHARE_CONTENT_KEY"
        const val TAG = "ShareContentFragment"
    }
}