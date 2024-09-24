package com.example.smartstudy.presentation.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    isDialogOpen: Boolean,
    title: String,
    bodyText: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {

    if(isDialogOpen) {

        AlertDialog(
            title = { Text(text = title) },
            text = {Text(text = bodyText) },
            onDismissRequest = {onDismissRequest()},
            confirmButton = {
                TextButton(
                    onClick = {onConfirmButtonClick()},
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {onDismissRequest()}) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}