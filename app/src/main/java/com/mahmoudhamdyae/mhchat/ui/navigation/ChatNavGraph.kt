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

/**
 * Provides Navigation graph for the application.
 */
@ExperimentalMaterialApi
fun NavGraphBuilder.chatGraph(appState: ChatAppState) {
    composable(HomeDestination.route) {
        HomeScreen(
            openAndPopUp = { route ->
                appState.clearAndNavigate(route)
            },
            openScreen = { route ->
                appState.navigate(route)
            }
        )
    }

    composable(LogInDestination.route) {
        LogInScreen(
            openAndPopUp = { route ->
                appState.clearAndNavigate(route)
            },
        )
    }

    composable(SignUpDestination.route) {
        SignUpScreen(
            openAndPopUp = { route ->
                appState.clearAndNavigate(route)
            }
        )
    }

    composable(SettingsDestination.route) {
        SettingsScreen(
            openAndPopUp = { route ->
                appState.clearAndNavigate(route)
            },
            navigateUp = {
                appState.popUp()
            }
        )
    }

    composable(UsersDestination.route) {
        UsersScreen(
            navigateUp = {
                appState.popUp()
            }
        )
    }
}