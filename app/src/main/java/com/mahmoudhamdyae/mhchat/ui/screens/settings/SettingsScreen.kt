package com.mahmoudhamdyae.mhchat.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.fieldModifier
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.composable.DialogCancelButton
import com.mahmoudhamdyae.mhchat.ui.composable.DialogConfirmButton
import com.mahmoudhamdyae.mhchat.ui.composable.PasswordField
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object SettingsDestination: NavigationDestination {
    override val route: String = "settings"
    override val titleRes: Int = R.string.settings_screen_title
}

@Composable
fun SettingsScreen(
    openAndClear: (String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }

    Column(modifier = modifier) {
        BasicToolBar(
            title = SettingsDestination.titleRes,
            canNavigateUp = true,
            navigateUp = navigateUp
        )
        Button(onClick = {
            showWarningDialog = true
        }) {
            Text(text = stringResource(id = R.string.delete_my_account))
        }
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = {
                Column(modifier = modifier) {
                    Text(
                        text = stringResource(R.string.delete_account_description),
                        style = TextStyle(color = MaterialTheme.colorScheme.error)
                    )
                    Text(
                        stringResource(R.string.delete_account_description_enter_fields),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    PasswordField(
                        value = password,
                        onNewValue = { password = it },
                        focusManager = LocalFocusManager.current,
                        passwordError = null,
                        modifier = Modifier.fieldModifier()
                    )
                }
                   },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.delete_account_confirm) {
                    viewModel.onDeleteAccount(password, openAndClear)
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}