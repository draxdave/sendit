package com.drax.sendit.view.messages.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drax.sendit.view.messages.MessageUiModel
import kotlinx.coroutines.launch


@Composable
fun MessagesListLayout(
    modifier: Modifier = Modifier,
    onShareMessage: (String) -> Unit,
    onCopyMessage: (String) -> Unit,
    onRemove: (Long) -> Unit,
    messagesList: List<MessageUiModel>,
) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var autoscrollVisibility by remember { mutableStateOf(false) }
    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }
            .collect {
                autoscrollVisibility = it != 0
            }
    }

    Box(
        modifier = modifier
            .background(color = MaterialTheme.colors.background)
    ) {
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
                    onShareMessage = { onShareMessage(item.message) },
                    onCopyMessage = { onCopyMessage(item.message) },
                    onRemoveMessage = { onRemove(item.id) }
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = autoscrollVisibility,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            Text(
                text = "Latest Messages",
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = CircleShape
                    )
                    .padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                    .clickable {
                        scope.launch {
                            state.animateScrollToItem(0)
                        }
                    }
            )
        }
    }
}


@Preview
@Composable
fun MessagesScreenPreview() {
    MessagesListLayout(
        onShareMessage = {},
        onCopyMessage = {},
        onRemove = {},
        messagesList = TEMP_MESSAGES
    )
}

internal val TEMP_MESSAGES = listOf(
    MessageUiModel(
        id = 11,
        isSender = true,
        message = "Simple short",
        partyName = "Siamak",
        addedDate = "3 Feb 2022 12:45",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 12,
        isSender = true,
        message = "Simple Long message. Simple Long message. Simple Long message. \nSimple Long" +
                " message. Simple Long message.",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        addedDate = "4 Feb 2022 12:45",
    ),
    MessageUiModel(
        id = 13,
        isSender = false,
        message = "Links with Some message text or url like google.com http://some.com also\n" +
                " +986546232 f@g.r short",
        partyName = "Behnaz Moradi",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        addedDate = "6 Feb 2022 12:45",
    ),
    MessageUiModel(
        id = 14,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 15,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 16,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 17,
        isSender = true,
        message = "Simple short",
        partyName = "Siamak",
        addedDate = "3 Feb 2022 12:45",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 18,
        isSender = true,
        message = "Simple Long message. Simple Long message. Simple Long message. \nSimple Long" +
                " message. Simple Long message.",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        addedDate = "4 Feb 2022 12:45",
    ),
    MessageUiModel(
        id = 19,
        isSender = false,
        message = "Links with Some message text or url like google.com http://some.com also\n" +
                " +986546232 f@g.r short",
        partyName = "Behnaz Moradi",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        addedDate = "6 Feb 2022 12:45",
    ),
    MessageUiModel(
        id = 20,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 21,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 22,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
    MessageUiModel(
        id = 10,
        isSender = true,
        message = "Normal message",
        addedDate = "6 Feb 2022 12:45",
        partyName = "Siamak",
        thumbnail = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
    ),
)