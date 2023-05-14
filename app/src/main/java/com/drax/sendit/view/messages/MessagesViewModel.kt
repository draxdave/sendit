package com.drax.sendit.view.messages

import androidx.lifecycle.ViewModel
import com.drax.sendit.data.db.model.Connection
import com.drax.sendit.data.db.model.DeviceDomain
import com.drax.sendit.data.db.model.Transaction

class MessagesViewModel : ViewModel() {
    val testMessages = listOf(
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
}

data class MessageUiModel(
    val id: Long,
    val isSender: Boolean,
    val message: String,
    val addedDate: String,
    val partyName: String,
    val thumbnail: String,
)