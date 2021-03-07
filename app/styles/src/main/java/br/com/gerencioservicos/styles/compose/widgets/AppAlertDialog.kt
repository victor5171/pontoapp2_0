package br.com.gerencioservicos.styles.compose.widgets

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppAlertDialog(
    key: Any,
    title: String,
    message: String,
    confirmText: String,
    cancelText: String? = null,
    onConfirm: (() -> Unit)? = null
) {
    var showingDialog by remember(key) { mutableStateOf(true) }

    if (showingDialog) {
        AlertDialog(
            onDismissRequest = { showingDialog = false },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    showingDialog = false
                    onConfirm?.invoke()
                }) {
                    Text(confirmText)
                }
            },
            dismissButton = cancelText?.let {
                {
                    Button(onClick = { showingDialog = false }) {
                        Text(cancelText)
                    }
                }
            }
        )
    }
}
