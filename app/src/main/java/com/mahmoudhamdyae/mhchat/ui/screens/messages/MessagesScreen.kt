package com.mahmoudhamdyae.mhchat.ui.screens.messages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.AssetParamType
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.composable.EmptyScreen
import com.mahmoudhamdyae.mhchat.ui.composable.ProfileImage
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.resources.LocalSpacing
import com.mahmoudhamdyae.mhchat.ui.screens.profile.ProfileDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

object MessagesDestination: NavigationDestination {
    override val route: String = "messages"
    override val titleRes: Int = R.string.messages_screen_title
    const val chatIdArg = "chatId"
    const val userArg = "user"
    val routeWithArgs = "$route/{$chatIdArg}/{$userArg}"
    val arguments = listOf(
        navArgument(chatIdArg) { type = NavType.StringType},
        navArgument(userArg) { type = AssetParamType() }
    )
}

@Composable
fun MessagesScreen(
    navigateUp: () -> Unit,
    openScreen: (String) -> Unit,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier,
    viewModel: MessagesViewModel,
) {
    val messages: List<Message>? =
        viewModel.chat.collectAsStateWithLifecycle(initialValue = Chat()).value?.messages?.reversed()

    if (messages == null || messages.isEmpty()) {
        EmptyScreen()
    } else {
        MessagesScreenContent(
            navigateUp = navigateUp,
            openScreen = openScreen,
            messages = messages,
            onMessageSend = viewModel::onMessageSend,
            imageProfile = imageProfile,
            userName = userName,
            modifier = modifier
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreenContent(
    navigateUp: () -> Unit,
    openScreen: (String) -> Unit,
    messages: List<Message>,
    onMessageSend: (String) -> Unit,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                MessagesList(
                    messages = messages,
                    imageProfile = imageProfile,
                    userName = userName,
                    openScreen = openScreen,
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )
                UserInput(
                    resetScroll = {
                        scope.launch {
                            scrollState.scrollToItem(0)
                        }
                    },
                    modifier = Modifier.padding(LocalSpacing.current.medium)
                ) { onMessageSend(it) }
            }

            BasicToolBar(
                title = MessagesDestination.titleRes,
                canNavigateUp = true,
                navigateUp = navigateUp
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MessagesList(
    messages: List<Message>,
    imageProfile: String?,
    userName: String,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {

        val authorMe = stringResource(id = R.string.author_me)

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

            LazyColumn(
                reverseLayout = true,
                state = scrollState,
                // Add content padding so that the content can be scrolled (y-axis)
                // below the status bar + app bar
                contentPadding =
                WindowInsets.statusBars.add(WindowInsets(top = 90.dp)).asPaddingValues(),
                modifier = Modifier.fillMaxSize()
            ) {
//                for (index in messages.indices) {
//                    val prevAuthor = messages.getOrNull(index - 1)?.author
//                    val nextAuthor = messages.getOrNull(index + 1)?.author
//                    val content = messages[index]
//                    val isFirstMessageByAuthor = prevAuthor != content.author
//                    val isLastMessageByAuthor = nextAuthor != content.author

                    val messagesGrouped = messages.groupBy { it.dayOfMonth }

                    messagesGrouped.forEach { (date, messages) ->
                        val today = SimpleDateFormat("d MMM").format(Date()) == date
                        val dayString = if (today) "Today" else date
                        item(key = date) {
                            DayHeader(
                                dayString,
                                modifier = Modifier
                                    // Animate each list item to slide in vertically
                                    .animateEnterExit(
                                        enter = slideInVertically(
                                            animationSpec = spring(
                                                stiffness = Spring.StiffnessVeryLow,
                                                dampingRatio = Spring.DampingRatioLowBouncy
                                            ),
                                        )
                                    ))
                        }

                        messages.forEachIndexed { index, message ->
                            val prevAuthor = messages.getOrNull(index - 1)?.author
                            val nextAuthor = messages.getOrNull(index + 1)?.author
                            val content = messages[index]
                            val isFirstMessageByAuthor = prevAuthor != content.author
                            val isLastMessageByAuthor = nextAuthor != content.author
                            item(key = message.messageId) {
                                MessageListItem(
                                    message = message,
                                    imageProfile = imageProfile,
                                    userName = userName,
                                    isUserMe = message.author == authorMe,
                                    isFirstMessageByAuthor = isFirstMessageByAuthor,
                                    isLastMessageByAuthor = isLastMessageByAuthor,
                                    openScreen = openScreen,
                                    modifier = Modifier
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
        }

        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MessageListItem(
    message: Message,
    imageProfile: String?,
    userName: String,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) modifier.padding(top = 8.dp) else modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            ProfileImage(
                modifier = Modifier
                    .clickable(onClick = { openScreen(ProfileDestination.route) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .align(Alignment.Top),
                photoUri = imageProfile?.toUri()
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            message = message,
            userName = userName,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
//            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
            )
        }

//        message.image?.let {
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(
//                color = backgroundBubbleColor,
//                shape = ChatBubbleShape
//            ) {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = stringResource(id = R.string.attached_image)
//                )
//            }
//        }
    }
}

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean
) {
    val uriHandler = LocalUriHandler.current

//    val styledMessage = messageFormatter(
//        text = message.body,
//        primary = isUserMe
//    )

    Text(
        text = message.body,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
    )

//    ClickableText(
//        text = styledMessage,
//        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
//        modifier = Modifier.padding(16.dp),
//        onClick = {
//            styledMessage
//                .getStringAnnotations(start = it, end = it)
//                .firstOrNull()
//                ?.let { annotation ->
//                    when (annotation.tag) {
//                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
//                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
//                        else -> Unit
//                    }
//                }
//        }
//    )
}

@Composable
fun AuthorAndTextMessage(
    message: Message,
    userName: String,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(message, userName)
        }
        ChatItemBubble(message, isUserMe,)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun DayHeader(
    dayString: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
private fun AuthorNameTimestamp(message: Message, userName: String) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = userName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message.time,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
fun MessageListItemPreview() {
    val fakeMessage = Message(body = "Hello")
    MessageListItem(
        message = fakeMessage,
        imageProfile = null,
        userName = "Mahmoud",
        isFirstMessageByAuthor = false,
        isLastMessageByAuthor = true,
        isUserMe = false,
        openScreen = {}
    )
}

@Preview
@Composable
fun MessagesListPreview() {
    val fakeMessages = listOf(
        Message(body = "Hello"),
        Message(body = "Hello2")
    )
    MessagesList(
        messages = fakeMessages,
        imageProfile = null,
        userName = "Mahmoud",
        openScreen = {},
    )
}

@Preview
@Composable
fun MessagesScreenContentPreview() {
    val fakeMessages = listOf(
        Message(body = "Hello"),
        Message(body = "Hello2")
    )
    MessagesScreenContent(
        navigateUp = {},
        messages = fakeMessages,
        onMessageSend = {},
        imageProfile = null,
        userName = "Mahmoud",
        openScreen = {},
    )
}

private val JumpToBottomThreshold = 56.dp