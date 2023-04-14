package com.mahmoudhamdyae.mhchat.ui

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.SignOutDialog
import com.mahmoudhamdyae.mhchat.ui.navigation.chatGraph
import com.mahmoudhamdyae.mhchat.ui.screens.home.DrawerContent
import com.mahmoudhamdyae.mhchat.ui.screens.home.DrawerElement
import com.mahmoudhamdyae.mhchat.ui.screens.home.DrawerViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ChatApp(
    modifier: Modifier = Modifier,
    viewModel: DrawerViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val appState = rememberAppState(snackBarHostState = snackBarHostState)

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val selectedItem = remember { mutableStateOf(DrawerElement.NONE) }
    val scope = rememberCoroutineScope()
    val openScreen: (String) -> Unit = { appState.navController.navigate(it) { launchSingleTop = true } }
    var showWarningDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentUser = User(userName = "haha", email = "hehe"),
                openScreen = openScreen,
                onSignOut = { showWarningDialog = true },
                selectedItem = selectedItem,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackBarData ->
                        Snackbar(snackBarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                    }
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = appState.navController,
                startDestination = HomeDestination.route,
                modifier = modifier.padding(innerPadding)
            ) {
                chatGraph(appState) { scope.launch { drawerState.open() } }
            }
        }
    }

    if (showWarningDialog) {
        SignOutDialog(
            cancelAction = { showWarningDialog = false},
            action = {
                viewModel.onSignOut(openScreen)
                showWarningDialog = false
            }
        )
    }
}

@Composable
fun rememberAppState(
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackBarManager = SnackBarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackBarHostState, navController, snackBarManager, resources, coroutineScope) {
        ChatAppState(snackBarHostState, navController, snackBarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}