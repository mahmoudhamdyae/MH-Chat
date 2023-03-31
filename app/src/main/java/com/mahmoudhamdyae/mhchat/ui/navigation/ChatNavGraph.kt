package com.mahmoudhamdyae.mhchat.ui.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mahmoudhamdyae.mhchat.ui.ChatAppState
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeScreen
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInScreen
import com.mahmoudhamdyae.mhchat.ui.screens.signup.SignUpDestination
import com.mahmoudhamdyae.mhchat.ui.screens.signup.SignUpScreen
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersScreen
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsScreen
import com.mahmoudhamdyae.mhchat.ui.screens.signup.ProfileImageDestination
import com.mahmoudhamdyae.mhchat.ui.screens.signup.ProfileImageScreen

/**
 * Provides Navigation graph for the application.
 */
@ExperimentalMaterialApi
fun NavGraphBuilder.chatGraph(appState: ChatAppState) {

    val openScreen: (String) -> Unit = { route -> appState.navigate(route) }
    val openAndPopUp: (String) -> Unit = { route -> appState.clearAndNavigate(route) }
    val navigateUp = { appState.popUp() }

    composable(HomeDestination.route) {
        HomeScreen(
            openAndPopUp = openAndPopUp,
            openScreen = openScreen
        )
    }

    composable(LogInDestination.route) {
        LogInScreen(
            openAndPopUp = openAndPopUp,
        )
    }

    composable(SignUpDestination.route) {
        SignUpScreen(
            openAndPopUp = openAndPopUp,
            openScreen = openScreen
        )
    }

    composable(SettingsDestination.route) {
        SettingsScreen(
            openAndPopUp = openAndPopUp,
            navigateUp = navigateUp
        )
    }

    composable(UsersDestination.route) {
        UsersScreen(
            navigateUp = navigateUp
        )
    }

    composable(ProfileImageDestination.route) {
        ProfileImageScreen(
            openScreen = openScreen
        )
    }
}