package com.mahmoudhamdyae.mhchat.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object ProfileDestination: NavigationDestination {
    override val route: String = "profile"
    override val titleRes: Int = R.string.profile_screen_title
}

@Composable
fun ProfileScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    BasicToolBar(
        title = ProfileDestination.titleRes,
        canNavigateUp = true,
        navigateUp = navigateUp
    )
}