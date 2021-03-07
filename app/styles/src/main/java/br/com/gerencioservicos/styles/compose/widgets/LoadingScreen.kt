package br.com.gerencioservicos.styles.compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.gerencioservicos.styles.R
import br.com.gerencioservicos.styles.compose.Dimens
import br.com.gerencioservicos.styles.compose.Paddings

@Preview
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(Dimens.LoadingSize))
            Spacer(modifier = Modifier.size(Paddings.Small))
            Text(
                modifier = Modifier.wrapContentSize(align = Alignment.Center),
                text = stringResource(id = R.string.loading),
                style = MaterialTheme.typography.body1
            )
        }
    }
}
