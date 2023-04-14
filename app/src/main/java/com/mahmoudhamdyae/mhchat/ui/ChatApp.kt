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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
fun ChatApp(
    viewModel: DrawerViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val appState = rememberAppState(snackBarHostState = snackBarHostState)

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val selectedItem = remember { mutableStateOf(DrawerElement.NONE) }
    val scope = rememberCoroutineScope()
    val openScreen: (String) -> Unit = { appState.navController.navigate(it) { launchSingleTop = true } }
    var showWarningDialog by remember { mutableStateOf(false) }
    val currentUser: User? = viewModel.currentUser.collectAsStateWithLifecycle(User()).value

    if (currentUser?.userName?.isNotEmpty() == true) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    currentUser = currentUser,
                    openScreen = openScreen,
                    onSignOut = { showWarningDialog = true },
                    selectedItem = selectedItem,
                    closeDrawer = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            ChatAppContent(snackBarHostState, appState, currentUser, scope, drawerState)
        }
    } else {
        ChatAppContent(snackBarHostState, appState, currentUser, scope, drawerState)
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
@OptIn(ExperimentalPagerApi::class)
private fun ChatAppContent(
    snackBarHostState: SnackbarHostState,
    appState: ChatAppState,
    currentUser: User?,
    scope: CoroutineScope,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
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
            chatGraph(appState) {
                if (currentUser?.userName?.isNotEmpty() == true) {
                    scope.launch { drawerState.open() }
                }
            }
        }
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