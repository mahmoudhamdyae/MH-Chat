package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination

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
        topBar = { BasicToolBar(title = HomeDestination.titleRes) },
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