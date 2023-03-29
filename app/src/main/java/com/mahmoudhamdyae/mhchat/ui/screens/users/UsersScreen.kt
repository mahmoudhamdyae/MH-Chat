package com.mahmoudhamdyae.mhchat.ui.screens.users

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object UsersDestination: NavigationDestination {
    override val route: String = "users"
    override val titleRes: Int = R.string.users_screen_title
}

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier
) {
}