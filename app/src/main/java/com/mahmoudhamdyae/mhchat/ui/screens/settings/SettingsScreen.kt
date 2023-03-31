package com.mahmoudhamdyae.mhchat.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.fieldModifier
import com.mahmoudhamdyae.mhchat.ui.composable.*
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object SettingsDestination: NavigationDestination {
    override val route: String = "settings"
    override val titleRes: Int = R.string.settings_screen_title
}

@Composable
fun SettingsScreen(
    openAndPopUp: (String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    Column(modifier = modifier) {
        BasicToolBar(
            title = SettingsDestination.titleRes,
            canNavigate = true,
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
                    Text(stringResource(R.string.delete_account_description))
                    Text(
                        stringResource(R.string.delete_account_description_enter_fields),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    PasswordField(
                        value = password,
                        onNewValue = { password = it },
                        focusManager = LocalFocusManager.current,
                        modifier = Modifier.fieldModifier()
                    )
                }
                   },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.delete_account_confirm) {
                    viewModel.onDeleteAccount(password, openAndPopUp)
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}