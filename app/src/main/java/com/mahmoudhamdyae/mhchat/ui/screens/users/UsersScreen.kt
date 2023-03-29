package com.mahmoudhamdyae.mhchat.ui.screens.users

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object UsersDestination: NavigationDestination {
    override val route: String = "users"
    override val titleRes: Int = R.string.users_screen_title
}

@Composable
fun UsersScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BasicToolBar(
            title = UsersDestination.titleRes,
            canNavigate = true,
            navigateUp = navigateUp
        )
    }
}