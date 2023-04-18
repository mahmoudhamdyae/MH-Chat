package com.mahmoudhamdyae.mhchat.ui.screens.users

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.composable.EmptyScreen
import com.mahmoudhamdyae.mhchat.ui.composable.ProfileImage
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination

object UsersDestination: NavigationDestination {
    override val route: String = "users"
    override val titleRes: Int = R.string.users_screen_title
}

@Composable
fun UsersScreen(
    openScreen: (String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val users = viewModel.users.collectAsStateWithLifecycle(initialValue = emptyList())

    UsersScreenContent(
        navigateUp = navigateUp,
        users = users.value,
        openScreen = openScreen,
        onItemClick = viewModel::onItemClick,
        modifier = modifier,
    )
}

@Composable
private fun UsersScreenContent(
    navigateUp: () -> Unit,
    users: List<User>,
    openScreen: (String) -> Unit,
    onItemClick: (User, (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicToolBar(
            title = UsersDestination.titleRes,
            canNavigateUp = true,
            navigateUp = navigateUp
        )
        if (users.isEmpty()) {
            EmptyScreen()
        } else {
            UsersList(
                openScreen = openScreen,
                onItemClick = onItemClick,
                users = users,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UsersList(
    openScreen: (String) -> Unit,
    onItemClick: (User, (String) -> Unit) -> Unit,
    users: List<User>,
    modifier: Modifier = Modifier,
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
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
    ) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(users, key = { _, user ->
                user.userId
            }) { index, user ->
                UserListItem(
                    openScreen = openScreen,
                    onItemClick = onItemClick,
                    user = user,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = StiffnessVeryLow,
                                    dampingRatio = DampingRatioLowBouncy
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
fun UserListItem(
    openScreen: (String) -> Unit,
    onItemClick: (User, (String) -> Unit) -> Unit,
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier,
        onClick = { onItemClick(user, openScreen) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(72.dp)
        ) {
            ProfileImage(
                photoUri = user.imageUrl?.toUri(),
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.bodyLarge
                        .copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun UserListItem() {
    val fakeUser = User(userName = "Mahmoud")
    UserListItem(openScreen = {}, onItemClick = { _, _ -> }, user = fakeUser)
}

@Preview
@Composable
fun UsersListPreview() {
    val fakeUsers = listOf(
        User(userName = "Mahmoud"),
        User(userName = "Ahmed")
    )
    UsersList(openScreen = {}, onItemClick = { _, _ -> }, users = fakeUsers)
}

@Preview
@Composable
fun UsersScreenContentPreview() {
    val fakeUsers = listOf(
        User(userName = "Mahmoud"),
        User(userName = "Ahmed")
    )
    UsersScreenContent(navigateUp = {}, users = fakeUsers, openScreen = {}, onItemClick = { _, _ ->})
}