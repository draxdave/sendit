package com.drax.sendit.view.qr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import coil.compose.AsyncImage

@Composable
fun QrDisplayBanner(
    modifier: Modifier = Modifier,
    uiState: QrLoadState,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .aspectRatio(1f)
                .align(Alignment.Center),
        ) {

            Image(
                modifier = Modifier
                    .fillMaxSize(.5f)
                    .align(Alignment.Center)
                    .alpha(.7f)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_add_device_qr_24),
                contentDescription = "ic_add_device_qr_24",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
            )

            if (uiState !is QrLoadState.Success)
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(.5f)
                        .align(Alignment.Center),
                    strokeWidth = 7.dp,
                    strokeCap = Round,
                    color = MaterialTheme.colors.secondary.copy(alpha = .4f),
                )

            (uiState as? QrLoadState.Success)?.run {
                Card(
                    modifier = Modifier
                        .padding(7.dp)
                        .fillMaxSize(),
                    elevation = 7.dp,
                ) {
                    AsyncImage(
                        model = qrImageUrl,
                        contentDescription = "Qr code for this device",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrDisplayLoadingPreview() {
    val uiState by remember { mutableStateOf(QrLoadState.Loading) }
    QrDisplayBanner(
        uiState = uiState
    )
}

@Preview(showBackground = true)
@Composable
fun QrDisplaySuccessPreview() {
    val uiState by remember {
        mutableStateOf(
            QrLoadState.Success(
                qrImageUrl = "https://www.qrstuff.com/images/default_qrcode.png"
            )
        )
    }
    QrDisplayBanner(
        uiState = uiState
    )
}

sealed class QrLoadState {
    object Loading : QrLoadState()
    data class Success(val qrImageUrl: String) : QrLoadState()
    object Error : QrLoadState()
}