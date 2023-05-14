package com.drax.sendit.view.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import coil.compose.AsyncImage
import com.drax.sendit.view.composeUtil.LinkifyText


private const val MESSAGE_CARD_MIN_WIDTH_DP = 120

@Composable
fun MessageItem(
    senderName: String,
    message: String,
    dateTime: String,
    messageProfileIconUrl: String,
    isSender: Boolean,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = messageProfileIconUrl,
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, end = 4.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.low_white))
                ,
            )

            Column(
                modifier = Modifier

            ) {
                val messageBackgroundColor = colorResource(
                    if (isSender) {
                        R.color.senderTextBackground
                    } else {
                        R.color.receiverTextBackground
                    }
                )

                Text(
                    text = senderName,
                    maxLines = 1,
                    color = colorResource(id = R.color.main_text_lighter),
                    fontSize = MaterialTheme.typography.caption.fontSize,
                    modifier = Modifier
                        .background(
                            shape = CircleShape,
                            color = colorResource(id = R.color.low_white)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)

                )

                Card(
                    shape = RoundedCornerShape(7.dp),
                    backgroundColor = messageBackgroundColor,
                    elevation = 4.dp,
                    modifier = Modifier
                        .padding(top = 4.dp, end = 8.dp)
                        .widthIn(
                            min = MESSAGE_CARD_MIN_WIDTH_DP.dp
                        )
                ) {
                    LinkifyText(
                        text = message,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = dateTime,
                    maxLines = 1,
                    color = colorResource(id = R.color.main_text_lighter),
                    fontSize = MaterialTheme.typography.overline.fontSize,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SenderMessageItemPreview() {
    MessageItem(
        senderName = "Siamak Mahmoudi",
        message = "Some message text or url like google.com http://some.com also\n +986546232 f@g.ir",
        dateTime = "23 Feb 22",
        messageProfileIconUrl = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        isSender = true,
    )
}

@Preview(showBackground = true)
@Composable
fun ReceiverMessageItemPreview() {
    MessageItem(
        senderName = "Siamak Mahmoudi",
        message = "Some message text or url like google.com http://some.com also\n +986546232 f@g.ir",
        dateTime = "23 Feb 22",
        messageProfileIconUrl = "https://cdn-icons-png.flaticon.com/512/882/882704.png",
        isSender = false,
    )
}
