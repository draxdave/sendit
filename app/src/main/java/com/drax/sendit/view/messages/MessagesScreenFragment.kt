package com.drax.sendit.view.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.drax.sendit.view.base.BaseComposeFragment
import kotlinx.coroutines.launch

class MessagesScreenFragment : BaseComposeFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MessagesScreen()
            }
        }
    }


    @Composable
    fun MessagesScreen(
        modifier: Modifier = Modifier,
        viewmodel: MessagesViewModel = viewModel(),
    ) {

        MessagesListLayout(
            messagesList = viewmodel.testMessages
        )


    }

    @Composable
    fun MessagesListLayout(
        modifier: Modifier = Modifier,
        messagesList: List<MessageUiModel>
    ) {
        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val autoscrollVisibility by remember { mutableStateOf(true) }

        Box(modifier = modifier) {
            LazyColumn(
                modifier = modifier,
                reverseLayout = true,
                state = state,
            ) {

                items(
                    items = messagesList,
                    key = {
                        it.id
                    },
                    contentType = {
                        it.isSender
                    }
                ) { item ->
                    MessageItem(
                        senderName = item.partyName,
                        message = item.message,
                        dateTime = item.addedDate,
                        isSender = item.isSender,
                        messageProfileIconUrl = item.thumbnail,
                    )
                }
            }
            AnimatedVisibility(
                visible = autoscrollVisibility,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                Text(
                    text = "Latest Messages",
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                state.animateScrollToItem(0)
                            }
                        }
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 16.dp)
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = CircleShape
                        )
                        .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }

    @Preview
    @Composable
    fun MessagesScreenPreview() {
        MessagesScreen()
    }
}
