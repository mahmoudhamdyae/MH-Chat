package com.mahmoudhamdyae.mhchat.ui.screens.profile

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.AssetParamType
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object ProfileDestination: NavigationDestination {
    override val route: String = "profile"
    override val titleRes: Int = R.string.profile_screen_title
    const val isUserMe = "isUserMe"
    const val userArg = "user"
    val routeWithArgs = "$route/{$userArg}/{$isUserMe}"
    val arguments = listOf(
        navArgument(isUserMe) { type = NavType.BoolType },
        navArgument(userArg) { type = AssetParamType() },
    )
}

@Composable
fun ProfileScreen(
    user: User,
    isUserMe: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    Toast.makeText(context, isUserMe.toString(), Toast.LENGTH_SHORT).show()
    BasicToolBar(
        title = ProfileDestination.titleRes,
        canNavigateUp = true,
        navigateUp = navigateUp
    )
}