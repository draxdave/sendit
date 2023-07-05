package com.drax.sendit.view.messages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import app.siamak.sendit.BuildConfig
import app.siamak.sendit.R
import coil.compose.AsyncImage
import com.drax.sendit.view.base.BaseComposeFragment
import com.drax.sendit.view.messages.components.EmptyTransaction
import com.drax.sendit.view.messages.components.MessagesListLayout
import com.drax.sendit.view.messages.components.TEMP_MESSAGES
import com.drax.sendit.view.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : BaseComposeFragment() {
    val viewModel by viewModels<MessagesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MessagesScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    @Composable
    fun MessagesScreen(
        modifier: Modifier = Modifier,
        viewModel: MessagesViewModel,
    ) {
        val uiState by viewModel.uiState
        RootScreen(
            modifier = modifier,
            uiState = uiState,
            onRemoveRequest = {
                viewModel.removeTransaction(it)
            }
        )
    }

    @Composable
    fun RootScreen(
        modifier: Modifier = Modifier,
        uiState: UiState = UiState.Loading,
        onRemoveRequest: (Long) -> Unit,
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = CHAT_BG_IMG_URL,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
            )

            if (uiState is UiState.Empty)
                EmptyTransaction()

            (uiState as? UiState.WithMessages)?.run {
                MessagesListLayout(
                    modifier = modifier,
                    onShareMessage = { shareTransaction(it) },
                    onCopyMessage = { copyToClipboard(it) },
                    onRemove = onRemoveRequest,
                    messagesList = messages
                )
            }
        }
    }

    private fun copyToClipboard(content: String) {
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)
            ?.setPrimaryClip(
                ClipData.newPlainText(
                    getString(R.string.copied_text_label),
                    content
                )
            )
        toast(getString(R.string.copied_text_label))
    }


    private fun shareTransaction(content: String) {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)

        }.also {
            startActivity(Intent.createChooser(it, null))
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun EmptyMessagesScreenPreview() {
        RootScreen(
            uiState = UiState.Empty,
            onRemoveRequest = {},
        )
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun NonEmptyMessagesScreenPreview() {
        RootScreen(
            uiState = UiState.WithMessages(TEMP_MESSAGES),
            onRemoveRequest = {},
        )
    }

    companion object {
        private const val CHAT_BG_IMG_URL = "${BuildConfig.BASE_URL}/img/chat_background_01.jpg"
    }
}

sealed class UiState {
    object Empty : UiState()
    object Loading : UiState()
    data class WithMessages(val messages: List<MessageUiModel>) : UiState()

}