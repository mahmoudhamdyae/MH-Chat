package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.ActionToolbar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import com.mahmoudhamdyae.mhchat.ui.settings.SettingsDestination

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
    LaunchedEffect(Unit) {
        viewModel.initialize(openAndPopUp)
    }

    Scaffold(
        modifier = modifier,
        topBar = { ActionToolbar(
            title = HomeDestination.titleRes,
            endActionIcons = listOf(Icons.Default.ExitToApp, Icons.Default.Settings),
            endActions = listOf({
                viewModel.onSignOut(openScreen)
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
}