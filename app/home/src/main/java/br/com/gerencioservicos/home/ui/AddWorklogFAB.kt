package br.com.gerencioservicos.home.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.gerencioservicos.home.R
import br.com.gerencioservicos.styles.compose.Paddings
import java.util.Locale

@Composable
internal fun AddWorklogFAB(onClick: () -> Unit) {
    FloatingActionButton(
        content = {
            Row(
                modifier = Modifier.padding(horizontal = Paddings.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(id = R.drawable.ic_baseline_add_24), null)
                Spacer(Modifier.size(Paddings.Tiny))
                Text(stringResource(id = R.string.add_worklog).toUpperCase(Locale.getDefault()))
            }
        },
        onClick = onClick
    )
}
