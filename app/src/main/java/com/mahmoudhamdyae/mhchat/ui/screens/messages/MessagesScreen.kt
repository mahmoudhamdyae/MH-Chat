package com.mahmoudhamdyae.mhchat.ui.screens.messages

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.domain.models.Chat
import com.mahmoudhamdyae.mhchat.domain.models.Message
import com.mahmoudhamdyae.mhchat.ui.composable.BasicToolBar
import com.mahmoudhamdyae.mhchat.ui.composable.EmptyScreen
import com.mahmoudhamdyae.mhchat.ui.composable.ProfileImage
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.resources.LocalSpacing
import java.util.*

object MessagesDestination: NavigationDestination {
    override val route: String = "messages"
    override val titleRes: Int = R.string.messages_screen_title
    const val toUserIdArg = "userId"
    const val chatIdArg = "chatId"
    const val imageProfileArg = "imageProfileId"
    const val userNameArg = "userNameId"
    val routeWithArgs = "$route/{$toUserIdArg}/{$chatIdArg}/{$imageProfileArg}/{$userNameArg}"
    val arguments = listOf(
        navArgument(toUserIdArg) { type = NavType.StringType },
        navArgument(chatIdArg) { type = NavType.StringType},
        navArgument(imageProfileArg) { type = NavType.StringType },
        navArgument(userNameArg) { type = NavType.StringType }
    )
}

@Composable
fun MessagesScreen(
    navigateUp: () -> Unit,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier,
    viewModel: MessagesViewModel,
) {
    val messages: List<Message>? =
        viewModel.chat.collectAsStateWithLifecycle(initialValue = Chat()).value?.messages

    MessagesScreenContent(
        navigateUp = navigateUp,
        messages = messages,
        onMessageSend = viewModel::onMessageSend,
        imageProfile = imageProfile,
        userName = userName,
        modifier = modifier
    )
}

@Composable
private fun MessagesScreenContent(
    navigateUp: () -> Unit,
    messages: List<Message>?,
    onMessageSend: (String) -> Unit,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        BasicToolBar(
            title = MessagesDestination.titleRes,
            canNavigateUp = true,
            navigateUp = navigateUp
        )
        Spacer(modifier = Modifier.weight(1f))
        if (messages == null || messages.isEmpty()) {
            EmptyScreen()
        } else {
            MessagesList(messages = messages, imageProfile = imageProfile, userName = userName)
        }
        Footer(sendMessage = onMessageSend)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MessagesList(
    messages: List<Message>,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier
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
            itemsIndexed(messages, key = { _, message ->
                message.messageId
            }) { index, message ->
                MessageListItem(
                    message = message,
                    imageProfile = imageProfile,
                    userName = userName,
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

@Composable
fun MessageListItem(
    message: Message,
    imageProfile: String?,
    userName: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.padding(LocalSpacing.current.small)) {
        Row {
            ProfileImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                photoUri = imageProfile?.toUri()
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = userName,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                    Text(
                        text = message.body,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun Footer(
    sendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageBody by rememberSaveable { mutableStateOf("") }
    Row(modifier = modifier.padding(LocalSpacing.current.medium)) {
        TextField(
            value = messageBody,
            onValueChange = { messageBody = it},
            placeholder = { Text(text = stringResource(id = R.string.message_text_field_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    sendMessage(messageBody)
                    messageBody = ""
                }
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            sendMessage(messageBody)
            messageBody = ""
        }) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun FooterPreview() {
    Footer(sendMessage = {})
}

@Preview
@Composable
fun MessageListItemPreview() {
    val fakeMessage = Message(body = "Hello")
    MessageListItem(message = fakeMessage, imageProfile = null, userName = "Mahmoud")
}

@Preview
@Composable
fun MessagesListPreview() {
    val fakeMessages = listOf(
        Message(body = "Hello"),
        Message(body = "Hello2")
    )
    MessagesList(messages = fakeMessages, imageProfile = null, userName = "Mahmoud")
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
        userName = "Mahmoud"
    )
}