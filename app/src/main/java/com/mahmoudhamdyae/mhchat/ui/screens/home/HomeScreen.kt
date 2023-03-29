package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.ActionToolbar
import com.mahmoudhamdyae.mhchat.ui.composable.DialogCancelButton
import com.mahmoudhamdyae.mhchat.ui.composable.DialogConfirmButton
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home_screen_title
}

@Composable
fun HomeScreen(
    openAndPopUp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var showWarningDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initialize(openAndPopUp)
    }

    Scaffold(
        modifier = modifier,
        topBar = { ActionToolbar(
            title = HomeDestination.titleRes,
            endActionIcons = listOf(Icons.Default.ExitToApp, Icons.Default.Settings),
            endActions = listOf({
                showWarningDialog = true
            }, {
                openScreen(SettingsDestination.route)
            })
        ) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openScreen(UsersDestination.route)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.fab_add_content_description))
            }
        }
    )
    { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
        }
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.sign_out_title)) },
            text = { Text(stringResource(R.string.sign_out_description)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.sign_out_confirm) {
                    viewModel.onSignOut(openScreen)
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}