package com.drax.sendit.view.connections

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import coil.compose.AsyncImage
import com.drax.sendit.data.db.model.Connection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConnectionList(
    uiState: ConnectionUiState = ConnectionUiState.NoConnection,
    onRefresh: suspend () -> Unit,
    onUnpairConnectionClicked: (Connection) -> Unit
) {
    val refreshScope = rememberCoroutineScope()
    val refreshing = uiState is ConnectionUiState.Refreshing

    fun refresh() = refreshScope.launch {
        onRefresh.invoke()
    }

    val pullToRefreshState = rememberPullRefreshState(refreshing, ::refresh)


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullToRefreshState)
            .clipToBounds(),
    ) {
        if (uiState is ConnectionUiState.ConnectionsLoaded)
            LazyVerticalGrid(
                modifier = Modifier
                    .heightIn(120.dp, 1200.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
            ) {
                val listOfConnections = uiState.connectionList
                items(listOfConnections) { item ->
                    ConnectionItem(
                        iconUrl = item.connection.iconUrl,
                        deviceName = item.connection.name,
                        pairedSince = item.addedDate,
                        statusColor = item.connection.statusToColorRes(),
                        deviceStatus = item.connection.statusToStrRes(),
                    ) {
                        onUnpairConnectionClicked.invoke(item.connection)
                    }
                }
            }

        if (uiState is ConnectionUiState.NoConnection)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .align(alignment = Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = stringResource(id = R.string.no_connected_devices),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    color = colorResource(id = R.color.main_text_lighter),
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_phonelink_off_24),
                    contentDescription = null,
                )
            }

        PullRefreshIndicator(
            refreshing,
            pullToRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun ConnectionItem(
    iconUrl: String,
    deviceName: String,
    pairedSince: String,
    @StringRes deviceStatus: Int,
    @ColorRes statusColor: Int,
    onRemoveClick: () -> Unit = {},
) {

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.main_text_lighter))
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(120.dp)
                        .align(alignment = Alignment.Center)
                        .padding(16.dp),
                )

                Image(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            onRemoveClick.invoke()
                        }
                        .align(alignment = Alignment.TopEnd),
                    painter = painterResource(id = R.drawable.ic_baseline_delete_outline_24),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.background),
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(alignment = Alignment.BottomStart),
                    text = stringResource(id = deviceStatus),
                    color = colorResource(id = statusColor),
                    style = MaterialTheme.typography.body1,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(alignment = Alignment.CenterStart)
                ) {
                    Text(
                        text = deviceName,
                        color = colorResource(id = R.color.main_text_lighter),
                        style = MaterialTheme.typography.h6,
                    )
                    Text(
                        text = stringResource(
                            id = R.string.devices_item_device_added_date,
                            pairedSince
                        ),
                        color = colorResource(id = R.color.main_text_lighter),
                        style = MaterialTheme.typography.body1,
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ConnectionListPreview() {
    ConnectionList(
        onRefresh = {},
    ) {}
}

@Preview(showBackground = true)
@Composable
fun ConnectionItemPreview() {
    ConnectionItem(
        iconUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png",
        deviceName = "Device Name",
        pairedSince = "2021-01-01",
        deviceStatus = R.string.devices_status_pending,
        statusColor = R.color.main_text_lighter,
    )
}