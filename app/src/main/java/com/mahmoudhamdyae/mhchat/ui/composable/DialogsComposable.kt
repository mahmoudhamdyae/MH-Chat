package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mahmoudhamdyae.mhchat.R

@Composable
fun SignOutDialog(
    cancelAction: () -> Unit,
    action: () -> Unit,
) {
    AlertDialog(
        title = { Text(stringResource(R.string.sign_out_title)) },
        text = { Text(stringResource(R.string.sign_out_description)) },
        dismissButton = { DialogCancelButton(R.string.cancel, cancelAction) },
        confirmButton = {
            DialogConfirmButton(R.string.sign_out_confirm, action)
        },
        onDismissRequest = cancelAction
    )
}