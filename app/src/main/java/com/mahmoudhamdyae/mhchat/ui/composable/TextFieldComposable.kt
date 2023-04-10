package com.mahmoudhamdyae.mhchat.ui.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.mahmoudhamdyae.mhchat.R

@Composable
fun UserNameField(
    value: String,
    onNewValue: (String) -> Unit,
    focusManager: FocusManager,
    userNameError: Int?,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        isError = userNameError != null,
        placeholder = { Text(stringResource(R.string.user_name)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(id = R.string.user_name)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
    if (userNameError != null) {
        Text(
            text = stringResource(id = userNameError),
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}

@Composable
fun EmailField(
    value: String,
    onNewValue: (String) -> Unit,
    focusManager: FocusManager,
    emailError: Int?,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        isError = emailError != null,
        placeholder = { Text(stringResource(R.string.email)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = stringResource(id = R.string.email)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
    if (emailError != null) {
        Text(
            text = stringResource(id = emailError),
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}

@Composable
fun PasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    focusManager: FocusManager,
    passwordError: Int?,
    modifier: Modifier = Modifier,
) {
    PasswordField(value, R.string.password, onNewValue, passwordError, focusManager, modifier)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    focusManager: FocusManager,
    repeatedPasswordError: Int?,
    modifier: Modifier = Modifier,
) {
    PasswordField(value, R.string.repeat_password, onNewValue, repeatedPasswordError, focusManager, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    passwordError: Int?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    val icon =
        if (isVisible) painterResource(R.drawable.ic_visibility_on)
        else painterResource(R.drawable.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        isError = passwordError != null,
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(painter = icon, contentDescription = "Visibility")
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        visualTransformation = visualTransformation
    )
    if (passwordError != null) {
        Text(
            text = stringResource(id = passwordError),
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}