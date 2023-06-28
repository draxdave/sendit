package com.drax.sendit.view.messages.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.siamak.sendit.R


@Composable
fun EmptyTransaction() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_message_24),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                ,
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(id = R.string.no_transactions),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(16.dp),
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyTransactionPreview() {
    EmptyTransaction()
}
