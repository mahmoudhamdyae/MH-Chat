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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.*
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.settings.SettingsDestination
import com.mahmoudhamdyae.mhchat.ui.screens.users.UsersDestination
import kotlinx.coroutines.launch
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
    LaunchedEffect(Unit) {
        viewModel.initialize(openAndPopUp)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
    val selectedItem = remember { mutableStateOf(items[0]) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }) {
        val list by viewModel.uiState.collectAsStateWithLifecycle()
        HomeScreenContent(
            list = list,
            onItemClick = viewModel::onItemClick,
            onSignOut = viewModel::onSignOut,
            openScreen = openScreen,
            openDrawer = { scope.launch { drawerState.open() } },
            modifier = modifier
        )
    }
}

@Composable
fun HomeScreenContent(
    list: List<Pair<User?, Message?>>,
    onItemClick: (User, (String) -> Unit) -> Unit,
    onSignOut: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showWarningDialog by remember { mutableStateOf(false) }

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
            openDrawer = openDrawer
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
        },
    )
    { contentPadding ->
        if (list.isEmpty()) {
            EmptyScreen(modifier = modifier.padding(contentPadding))
        } else {
            ChatList(
                list = list,
                openScreen = openScreen,
                onItemClick = onItemClick,
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
                    onSignOut(openScreen)
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
    list: List<Pair<User?, Message?>>,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    onItemClick: (User, (String) -> Unit) -> Unit,
) {
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
            itemsIndexed(list, key = { _, list ->
                list.second!!.messageId
            }) { index, list ->
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
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message.body,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatListItemPreview() {
    val fakeUser = User(userName = "Mahmoud")
    val fakeMessage = Message(body = "Hello")
    ChatListItem(user = fakeUser, message = fakeMessage, openScreen = {}, onItemClick = { _, _ -> })
}

@Preview
@Composable
fun ChatListPreview() {
    val fakeUsers = listOf(
        User(userName = "Mahmoud"),
        User(userName = "Ahmed")
    )
    val fakeMessages = listOf(
        Message(body = "Hello"),
        Message(body = "Hello2")
    )
    val fakeList = listOf<Pair<User?, Message?>>(Pair(fakeUsers[0], fakeMessages[0]), Pair(fakeUsers[1], fakeMessages[1]))
    ChatList(list = fakeList, openScreen = {}, onItemClick = { _, _ ->})
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    val fakeUsers = listOf(
        User(userName = "Mahmoud"),
        User(userName = "Ahmed")
    )
    val fakeMessages = listOf(
        Message(body = "Hello"),
        Message(body = "Hello2")
    )
    val fakeList = listOf<Pair<User?, Message?>>(Pair(fakeUsers[0], fakeMessages[0]), Pair(fakeUsers[1], fakeMessages[1]))
    HomeScreenContent(
        list = fakeList,
        onItemClick = { _, _ ->},
        onSignOut = {},
        openScreen = {},
        openDrawer = {})
}