package com.mahmoudhamdyae.mhchat.ui.screens.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.basicButton
import com.mahmoudhamdyae.mhchat.common.ext.fieldModifier
import com.mahmoudhamdyae.mhchat.common.ext.textButton
import com.mahmoudhamdyae.mhchat.ui.composable.*
import com.mahmoudhamdyae.mhchat.ui.navigation.NavigationDestination
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormEvent
import com.mahmoudhamdyae.mhchat.ui.screens.auth.AuthFormState
import com.mahmoudhamdyae.mhchat.ui.screens.auth.ValidationEvent
import com.mahmoudhamdyae.mhchat.ui.screens.auth.login.LogInDestination

object SignUpDestination: NavigationDestination {
    override val route: String = "sign_up"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun SignUpScreen(
    openAndClear: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    viewModel.onSignUpClick(openScreen)
                }
            }
        }
    }

    SignUpScreenContent(
        openAndPopUp = openAndClear,
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
private fun SignUpScreenContent(
    openAndPopUp: (String) -> Unit,
    state: AuthFormState,
    onEvent: (AuthFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val fieldModifier = Modifier.fieldModifier()
    val focusManager = LocalFocusManager.current

    BasicToolBar(SignUpDestination.titleRes)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.sign_up_screen_title),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(modifier = Modifier.textButton(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.already_have_an_account))
            BasicTextButton(text = R.string.sign_in) {
                openAndPopUp(LogInDestination.route)
            }
        }

        UserNameField(
            value = state.userName,
            onNewValue = { onEvent(AuthFormEvent.UserNameChanged(it)) },
            userNameError = state.userNameError,
            focusManager = focusManager,
            modifier = fieldModifier
        )
        EmailField(
            value = state.email,
            onNewValue = { onEvent(AuthFormEvent.EmailChanged(it)) },
            emailError = state.emailError,
            focusManager = focusManager,
            modifier = fieldModifier
        )
        PasswordField(
            value = state.password,
            onNewValue = { onEvent(AuthFormEvent.PasswordChanged(it)) },
            passwordError = state.passwordError,
            focusManager = focusManager,
            modifier = fieldModifier
        )
        RepeatPasswordField(
            value = state.repeatedPassword,
            onNewValue = { onEvent(AuthFormEvent.RepeatedPasswordChanged(it)) },
            repeatedPasswordError = state.repeatedPasswordError,
            focusManager = focusManager,
            modifier = fieldModifier
        )

        BasicButton(R.string.sign_up_button, Modifier.basicButton()) {
            onEvent(AuthFormEvent.Submit)
        }
    }
}