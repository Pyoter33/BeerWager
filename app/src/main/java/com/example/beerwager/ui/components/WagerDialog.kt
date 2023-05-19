package com.example.beerwager.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R

@Composable
fun WagerYesCancelDialog(
    title: String,
    description: String,
    onPositive: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onPositive()
            }) {
                Text(
                    stringResource(id = R.string.text_yes),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(
                    stringResource(id = R.string.text_cancel),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}