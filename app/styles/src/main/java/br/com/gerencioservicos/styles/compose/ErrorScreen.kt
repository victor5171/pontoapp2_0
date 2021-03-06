package br.com.gerencioservicos.styles.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import br.com.gerencioservicos.styles.R

@Preview
@Composable
fun ErrorScreen(
    errorMessage: String = stringResource(id = R.string.generic_error_message),
    retryOnClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(Dimens.ErrorIconSize),
                painter = painterResource(id = R.drawable.ic__77_warning_sign),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(Paddings.Small))
            Text(
                textAlign = TextAlign.Center,
                text = errorMessage,
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.height(Paddings.Small))
            Text(
                modifier = Modifier.wrapContentSize(align = Alignment.Center).clickable { retryOnClick() },
                textDecoration = TextDecoration.Underline,
                text = stringResource(R.string.retry),
            )
        }
    }
}
