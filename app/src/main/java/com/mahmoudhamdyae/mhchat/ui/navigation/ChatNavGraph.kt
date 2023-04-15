package com.mahmoudhamdyae.mhchat.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.ChatAppState
import com.mahmoudhamdyae.mhchat.ui.composable.messagesViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInScreen
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.ProfileImageDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.ProfileImageScreen
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.SignUpDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.SignUpScreen
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeScreen
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesScreen
import com.mahmoudhamdyae.mhchat.ui.screens.notifications.NotificationsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.notifications.NotificationsScreen
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingDestination
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingScreen
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileDestination
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileScreen
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsScreen
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersScreen

/**
 * Provides Navigation graph for the application.
 */
@ExperimentalPagerApi
fun NavGraphBuilder.chatGraph(
    appState: ChatAppState,
    enableGestures: (Boolean) -> Unit,
    openDrawer: () -> Unit
) {

    val openScreen: (String) -> Unit = { route -> appState.navigate(route) }
    val openAndPopUp: (String) -> Unit = { route -> appState.clearAndNavigate(route) }
    val navigateUp = { appState.popUp() }

    composable(HomeDestination.route) {
        enableGestures(true)
        HomeScreen(
            openAndPopUp = openAndPopUp,
            openScreen = openScreen,
            openDrawer = openDrawer
        )
    }

    composable(LogInDestination.route) {
        enableGestures(false)
        LogInScreen(openAndPopUp = openAndPopUp,)
    }

    composable(SignUpDestination.route) {
        enableGestures(false)
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
            openScreen = openScreen,
            navigateUp = navigateUp
        )
    }

    composable(ProfileImageDestination.route) {
        enableGestures(false)
        ProfileImageScreen(openScreen = openScreen)
    }

    composable(
        route = MessagesDestination.routeWithArgs,
        arguments = MessagesDestination.arguments
    ) { navBackStack ->
        val chatId = navBackStack.arguments?.getString(MessagesDestination.chatIdArg)!!
        @Suppress("DEPRECATION")
        val user = navBackStack.arguments?.getParcelable<User>(MessagesDestination.userArg)!!
        MessagesScreen(
            navigateUp = navigateUp,
            openScreen = openScreen,
            anotherUser = user,
            viewModel = messagesViewModel(
                chatId = chatId,
            )
        )
    }

    composable(ProfileDestination.route) {
        ProfileScreen(
            navigateUp = navigateUp
        )
    }

    composable(OnBoardingDestination.route) {
        enableGestures(false)
        OnBoardingScreen(
            openAndPopUp = openAndPopUp
        )
    }

    composable(NotificationsDestination.route) {
        NotificationsScreen()
    }
}