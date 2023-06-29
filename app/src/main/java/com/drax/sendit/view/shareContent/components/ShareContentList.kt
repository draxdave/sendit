package com.drax.sendit.view.shareContent.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import com.drax.sendit.view.DeviceWrapper
import com.drax.sendit.view.connections.ConnectionsVM
import com.drax.sendit.view.shareContent.ShareContentUiState

@Composable
inline fun ShareContentList(
    modifier: Modifier = Modifier,
    uiState: ShareContentUiState,
    crossinline itemSelected: (Long) -> Unit = {},
) {

    Box(modifier = modifier) {
        if (uiState is ShareContentUiState.ConnectionsLoaded)
            LazyColumn {
                items(uiState.connections) { device ->
                    ShareItem(
                        title = device.connection.name,
                        subtitle = stringResource(
                            id = R.string.devices_item_device_added_date,
                            device.addedDate
                        ),
                        imageUrl = device.connection.iconUrl,
                        onClick = {
                            itemSelected.invoke(device.connection.id)
                        }
                    )
                }
            }

        if (uiState is ShareContentUiState.NoConnectionsAvailable)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 230.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.share_bottom_sheet_empty_list),
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = modifier.padding(top = 16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_device_unknown_24),
                    contentDescription = null
                )
            }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareContentListPreview() {
    ShareContentList(
        uiState = ShareContentUiState.ConnectionsLoaded(ConnectionsVM.TEMP_DEVICES)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareContentListEmptyPreview() {
    ShareContentList(
        uiState = ShareContentUiState.NoConnectionsAvailable
    )
}