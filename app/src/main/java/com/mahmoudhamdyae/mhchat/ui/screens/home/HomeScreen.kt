package com.mahmoudhamdyae.mhchat.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.*
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import java.util.*

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.home_screen_title
}

@Composable
fun HomeScreen(
    openAndPopUp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState
    var showWarningDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initialize(openAndPopUp)
    }

    Scaffold(
        modifier = modifier,
        topBar = { ActionToolbar(
            title = HomeDestination.titleRes,
            endActionIcons = listOf(Icons.Default.ExitToApp, Icons.Default.Settings),
            endActions = listOf({
                showWarningDialog = true
            }, {
                openScreen(SettingsDestination.route)
            }),
            onMainScreen = true,
        ) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    openScreen(UsersDestination.route)
                }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.fab_add_content_description))
            }
        }
    )
    { contentPadding ->
        if (uiState.users == null || uiState.users!!.isEmpty()) {
            EmptyScreen()
            ChatListItem(user = User(), message = Message(), openScreen = openScreen, onItemClick = viewModel::onItemClick)
        } else {
            ChatList(
                users = uiState.users!!,
                messages = uiState.lastMessages!!,
                openScreen = openScreen,
                onItemClick = viewModel::onItemClick,
                modifier = Modifier.padding(contentPadding)
            )
        }
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(R.string.sign_out_title)) },
            text = { Text(stringResource(R.string.sign_out_description)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.sign_out_confirm) {
                    viewModel.onSignOut(openScreen)
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ChatList(
    users: List<User?>,
    messages: List<Message?>,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (User, (String) -> Unit) -> Unit,
) {
    val list = users.zip(messages)
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    // Fade in entry animation for the entire list
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
    ) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(list) { index, list ->
                ChatListItem(
                    onItemClick = onItemClick,
                    openScreen = openScreen,
                    user = list.first,
                    message = list.second,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = Spring.StiffnessVeryLow,
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * (index + 1) } // staggered entrance
                            )
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListItem(
    user: User?,
    message: Message?,
    openScreen: (String) -> Unit,
    onItemClick: (User, (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    if (user != null && message != null) {
        Card(
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = modifier,
            onClick = { onItemClick(user, openScreen) },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .sizeIn(minHeight = 72.dp)
            ) {
                ProfileImage(
                    photoUri = user.imageUrl?.toUri(),
                    modifier = Modifier
                        .size(72.dp)
                )
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.userName,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}