package com.drax.sendit.view.qr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R
import com.drax.sendit.view.qr.QrUiState

@Composable
fun QrScanButton(
    modifier: Modifier = Modifier,
    uiState: QrUiState,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = CircleShape,
        onClick = { onClick() },
        enabled = uiState !is QrUiState.Loading,
    ) {

        Box(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_scan_qr),
                contentDescription = stringResource(id = R.string.scan),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(id = R.string.scan),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrScanButtonLoadingPreview() {
    QrScanButton(
        uiState = QrUiState.Loading,
        onClick = { }
    )
}

@Preview(showBackground = true)
@Composable
fun QrScanButtonPreview() {
    QrScanButton(
        uiState = QrUiState.Neutral,
        onClick = { }
    )
}
