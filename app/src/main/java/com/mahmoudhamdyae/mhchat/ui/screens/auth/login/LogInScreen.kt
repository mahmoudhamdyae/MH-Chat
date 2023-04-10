package com.mahmoudhamdyae.mhchat.ui.screens.auth.login

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
import com.mahmoudhamdyae.mhchat.ui.screens.auth.signup.SignUpDestination

object LogInDestination: NavigationDestination {
    override val route: String = "log_in"
    override val titleRes: Int = R.string.app_name

}

@Composable
fun LogInScreen(
    openAndPopUp: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogInViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    viewModel.onSignInClick(openAndPopUp)
                }
            }
        }
    }

    LogInScreenContent(
        openAndPopUp = openAndPopUp,
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
private fun LogInScreenContent(
    openAndPopUp: (String) -> Unit,
    state: AuthFormState,
    onEvent: (AuthFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val fieldModifier = Modifier.fieldModifier()
    val focusManager = LocalFocusManager.current

    BasicToolBar(title = LogInDestination.titleRes)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(id = R.string.log_in_screen_title),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(modifier = Modifier.textButton(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.new_in_app))
            BasicTextButton(text = R.string.sig_up_for_free) {
                openAndPopUp(SignUpDestination.route)
            }
        }
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

        BasicTextButton(R.string.forgot_password,
            Modifier
                .textButton()
                .align(Alignment.Start)) {
            onEvent(AuthFormEvent.ForgotPassword)
        }

        BasicButton(R.string.log_in_button, Modifier.basicButton()) {
            onEvent(AuthFormEvent.Submit)
        }
    }
}