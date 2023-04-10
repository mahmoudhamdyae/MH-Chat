package com.mahmoudhamdyae.mhchat.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mahmoudhamdyae.mhchat.ui.ChatAppState
import com.mahmoudhamdyae.mhchat.ui.composable.messagesViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeScreen
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInScreen
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesDestination
import com.mahmoudhamdyae.mhchat.ui.screens.messages.MessagesScreen
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingDestination
import com.mahmoudhamdyae.mhchat.ui.screens.onboarding.OnBoardingScreen
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileDestination
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileScreen
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsScreen
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.ProfileImageDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.ProfileImageScreen
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.SignUpDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.SignUpScreen
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersScreen

/**
 * Provides Navigation graph for the application.
 */
@ExperimentalPagerApi
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
        LogInScreen(openAndPopUp = openAndPopUp,)
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
            openScreen = openScreen,
            navigateUp = navigateUp
        )
    }

    composable(ProfileImageDestination.route) {
        ProfileImageScreen(openScreen = openScreen)
    }

    composable(
        route = MessagesDestination.routeWithArgs,
        arguments = MessagesDestination.arguments
    ) { navBackStack ->
        val toUserId = navBackStack.arguments?.getString(MessagesDestination.toUserIdArg)!!
        val chatId = navBackStack.arguments?.getString(MessagesDestination.chatIdArg)!!
        val imageProfileId = navBackStack.arguments?.getString(MessagesDestination.imageProfileArg)
        val userNameId = navBackStack.arguments?.getString(MessagesDestination.userNameArg)!!
        MessagesScreen(
            navigateUp = navigateUp,
            imageProfile = imageProfileId,
            userName = userNameId,
            viewModel = messagesViewModel(
                chatId = chatId,//"fde18e5a-bf96-4932-b050-f5aa0671133d",
                toUserId = toUserId,
            )
        )
    }

    composable(ProfileDestination.route) {
        ProfileScreen(
            navigateUp = navigateUp
        )
    }

    composable(OnBoardingDestination.route) {
        OnBoardingScreen(
            openAndPopUp = openAndPopUp
        )
    }
}