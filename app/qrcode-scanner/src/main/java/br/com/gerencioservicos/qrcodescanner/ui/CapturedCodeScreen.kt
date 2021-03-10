package br.com.gerencioservicos.qrcodescanner.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.gerencioservicos.qrcodescanner.R
import br.com.gerencioservicos.styles.compose.Colors
import br.com.gerencioservicos.styles.compose.Paddings
import br.com.gerencioservicos.styles.compose.Shapes

@Composable
internal fun CapturedCodeScreen(
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Disclaimer()
        Row(
            modifier = Modifier
                .padding(
                    start = Paddings.Small,
                    bottom = Paddings.Small,
                    end = Paddings.Small
                )
                .align(Alignment.BottomCenter)
        ) {
            ChildButton(
                onClick = { onSubmit() },
                imageResId = R.drawable.ic__07_32dp_check
            )
            Spacer(modifier = Modifier.width(Paddings.Small))
            ChildButton(
                onClick = { onCancel() },
                imageResId = R.drawable.ic__16_32dp_crossed
            )
        }
    }
}

@Composable
private fun RowScope.ChildButton(onClick: () -> Unit, imageResId: Int) {
    Button(
        modifier = Modifier.weight(1.0f),
        onClick = {
            onClick()
        }
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null
        )
    }
}

@Composable
private fun BoxScope.Disclaimer() {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clip(Shapes.SmallRoundedCornerShape)
            .align(Alignment.Center)
            .background(Colors.transparentWhite)
            .padding(Paddings.Small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.scanned_qrcode_title),
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(Paddings.Small))
        Image(
            painter = painterResource(R.drawable.ic_128dp_qr_code),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(Paddings.Small))
        Text(
            text = stringResource(R.string.scanned_qrcode_message),
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview
@Composable
private fun CapturedCodeScreenPreview() {
    CapturedCodeScreen(onSubmit = {}, onCancel = {})
}
