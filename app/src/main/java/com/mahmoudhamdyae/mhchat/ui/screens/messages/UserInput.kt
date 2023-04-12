package com.mahmoudhamdyae.mhchat.ui.screens.messages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.ui.resources.LocalSpacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit,
    onMessageSent: (String) -> Unit,
) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    Surface(tonalElevation = 2.dp) {
        Row(modifier = modifier) {
            UserInputText(
                textFieldValue = textState,
                onTextChanged = { textState = it },
                modifier = Modifier.weight(1f)
            ) {
                onMessageSent(textState.text)
                resetScroll()
                textState = TextFieldValue("")
            }
            UserInputButton(
                sendMessageEnabled = textState.text.isNotBlank(),
                onMessageSent = {
                    onMessageSent(textState.text)
                    textState = TextFieldValue("")
                    resetScroll()
                }
            )
        }
    }
}

@Composable
private fun UserInputButton(
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val border = if (!sendMessageEnabled) {
        BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        null
    }

    val disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    val buttonColors = ButtonDefaults.buttonColors(
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor
    )

    // Send button
    Button(
        modifier = modifier.padding(start = LocalSpacing.current.medium),
        enabled = sendMessageEnabled,
        onClick = onMessageSent,
        colors = buttonColors,
        border = border,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(imageVector = Icons.Default.Send, contentDescription = null)
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onMessageSent: () -> Unit,
) {
    TextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        placeholder = { Text(text = stringResource(id = R.string.message_text_field_label)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = { onMessageSent() }
        ),
        maxLines = 3,
        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    )
}

@Preview
@Composable
fun UserInputPreview() {
    UserInput(onMessageSent = {}, resetScroll = {})
}