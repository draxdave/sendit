package com.drax.sendit.view.connections

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R


@Composable
fun ConnectionsHeader(
    email: String,
    deviceInfo: String,
    lastUsed: String,
    onLogoutClick: () -> Unit,
) {
    var popupExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .height(200.dp),
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp)
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_fragment_profile),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.background),
                )
                Text(
                    text = email,
                    color = MaterialTheme.colors.background,
                )
            }

            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_device_unknown_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.background),
                )
                Text(
                    text = deviceInfo,
                    color = MaterialTheme.colors.background,
                )
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(id = R.string.last_used, lastUsed),
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.background.copy(alpha = 0.74f),
            )

        }
        Image(
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .fillMaxHeight(.5f)
                .padding(end = 32.dp),
            painter = painterResource(id = R.drawable.ic_launcher), contentDescription = null
        )
        Row(
            Modifier
                .align(alignment = Alignment.TopEnd)
        ) {
            DropdownMenu(
                modifier = Modifier,
                expanded = popupExpanded,
                onDismissRequest = {
                    popupExpanded = false
                }
            ) {
                DropdownMenuItem(
                    onClick = {
                        popupExpanded = false
                        onLogoutClick.invoke()
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = stringResource(id = R.string.sign_out),
                        style = MaterialTheme.typography.button,
                    )
                }
            }

            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        popupExpanded = true
                    },
                painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.background)
            )

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0000FF)
@Composable
fun ConnectionsHeaderPreview() {
    ConnectionsHeader(
        email = "email@s.com",
        deviceInfo = "Samsung A51 Ultra",
        lastUsed = "5 May 10:35",
        onLogoutClick = {}
    )
}
